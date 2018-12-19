package com.hlgf.sms.scheduler;

import com.hlgf.sms.Constants;
import com.hlgf.sms.model.SalesOrder;
import com.hlgf.sms.model.SmsSendLog;
import com.hlgf.sms.service.MsgSender;
import com.hlgf.sms.service.SalesOrderService;
import com.hlgf.sms.service.SmsSendLogService;
import com.hlgf.wx.WxConstants;
import com.hlgf.wx.message.TemplateSender;
import com.hlgf.wx.utils.FormatUtil;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 销售单短信通知任务，每60分钟执行一次
 *
 * @author ccr12312@163.com at 2018-11-7
 */
@Log4j2
@Data
@Component
public class SalesOrderNotificationTask {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String className = SalesOrderNotificationTask.class.getSimpleName();

    //当查询数据过多时，分批处理，每批处理个数
    private static final int excutorSize = 500;
    //每30分钟扫描发送
    private static final String CRON = "0 0/1 * * * ? ";
//    private static final String CRON = "0 0/30 * * * ? ";

    private static String PHONE_NUMBER_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

    @Value("${sms.template.sales-order.main}")
    private String template_main;

    @Value("${sms.template.sales-order.item}")
    private String template_item;

    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private SmsSendLogService logService;

    @Autowired
    private MsgSender msgSender;


    @Resource(name = "salesOrderWxSender")
    private TemplateSender templateSender;

    @Scheduled(cron = CRON)
    public void sendSMS() {
        long nano = System.currentTimeMillis();
        log.info("========{} job start scheduling at {}", className, dateFormat.format(new Date()));
        String lastSendTime = logService.findLastInsertTime(Constants.SmsType.SMS_TYPE_SALES_ORDER_NOTIFICATION);
        List<SalesOrder> salesOrderList = salesOrderService.findSalesOrderPassedRecently(lastSendTime);
        if (salesOrderList == null)
            salesOrderList = new ArrayList<>();
        long time1 = System.currentTimeMillis();
        log.info("{} job loaded List after {},loaded {} records , cost {} MS", className, lastSendTime, salesOrderList.size(), (time1 - nano));

        int size = salesOrderList.size();
        if (size > 0) {
            if (size > excutorSize) {
                int part = size / excutorSize;//分批数
                log.info("{} job total Data : {} ，Split into {} executions ", className, size, part);
                for (int i = 0; i < part; i++) {
                    List listPage = salesOrderList.subList(0, excutorSize);
                    long start = System.currentTimeMillis();
                    log.info("{} job start the {} execution", className, (i + 1));
                    executor(listPage);
                    long end = System.currentTimeMillis();
                    log.info("{} job end the {} execution , cost {} MS", className, (i + 1), (end - start));
                    salesOrderList.subList(0, excutorSize).clear();
                }

            }
            if (!salesOrderList.isEmpty()) {
                long start = System.currentTimeMillis();
                log.info("{} job start the last execution", className);
                executor(salesOrderList);
                long end = System.currentTimeMillis();
                log.info("{} job end the last execution , cost {} MS", className, (end - start));
            }
        }
        log.info("========{} job done scheduling at {} with {}ms", className, dateFormat.format(new Date()), System.currentTimeMillis() - nano);
    }

    /**
     * 处理获取的数据
     *
     * @param salesOrderList
     */
    private void executor(List<SalesOrder> salesOrderList) {
        long time0 = System.currentTimeMillis();
        log.info("{} job start to prepared data for Sms ", className);
        //批量查询smsSendLog,判断是否已经发送过
        Map<String, Integer> cntMap = new HashMap();
        List relatedList = new ArrayList();
        salesOrderList.forEach(x -> {
            relatedList.add(x.getPkRelated());
        });
        List<SmsSendLog> logList = logService.findList(null, relatedList, Constants.SmsType.SMS_TYPE_SALES_ORDER_NOTIFICATION, null);
        logList.forEach(x -> {
            if (cntMap.containsKey(x.getRelatedId())) {
                cntMap.put(x.getRelatedId(), 1);
            }
        });
        Map<String, SmsSendLog> smsMap = new HashMap<>();
        List<SalesOrder> wxList = new ArrayList<>();
        salesOrderList.stream().filter(x -> {
            if (!validateMobile(x.getMobile())) {
                log.error("{} invalid mobile number {},receiver is{} , related PK {} ignored.", className, x.getMobile(), x.getCustomerName(), x.getPkRelated());
                return false;
            } else {
                if (cntMap.containsKey(x.getPkRelated())) {
                    log.error("{} target mobile {}, related pk {} has already send.", className, x.getMobile(), x.getPkRelated());
                    return false;
                }
                if (WxConstants.getBindInfo().containsKey(x.getMobile())) {
                    x.setOpenId(WxConstants.getBindInfo().get(x.getMobile()));
                    wxList.add(x);
                    return false;
                }
                return true;
            }
        }).forEach(x -> {
            //销售单物料详情
            String itemContent = StringUtils.collectionToDelimitedString(x.getItemList().stream().map(
                    i -> String.format(template_item, i.getProductName(), i.getProductSize(),
                            FormatUtil.formatMoney(i.getPrice()), i.getCount(), i.getUnit())).collect(Collectors.toList()
            ), "；");
            //销售单详情
            String content = String.format(template_main, x.getGroupName(), x.getCustomerName(),
                    FormatUtil.transferDateToExactFormat(x.getPassDate()), x.getRelatedNum(), itemContent, FormatUtil.formatMoney(x.getTotalAmount()));

            SmsSendLog sms = x.toSmsSendLog();
            sms.setContent(content);
            sms.setSmsType(Constants.SmsType.SMS_TYPE_SALES_ORDER_NOTIFICATION);
            sms.setSendTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            sms.setWordCount(sms.getContent().length());
            sms.setSendCnt(1);
            smsMap.put(x.getPkRelated(), sms);
            //FIXME for test
            wxList.add(x);
        });

        long time1 = System.currentTimeMillis();
        log.info("{} job prepared for Sms end , cost {} ms", className, (time1 - time0));
        log.info("{} job send Sms start ", className);

        msgSender.betchSend(smsMap);
        for (SalesOrder enity : wxList) {
            templateSender.sendWxMsg(enity);
        }
        smsMap.entrySet().forEach(x -> {
            SmsSendLog sms = x.getValue();
            log.info("{} job's executor send Sms {} , receiver is {} ,mobile is {}, content is {}, ", className, sms.getSendState().equals("Y") ? "success" : "failure", sms.getReceiverName(), sms.getReceiverNumber(), sms.getContent());
        });
        long time2 = System.currentTimeMillis();
        log.info("{} job send Sms cost {} ms", className, (time2 - time1));
    }

    private boolean validateMobile(String mobile) {
        return null != mobile && !mobile.isEmpty() && Pattern.compile(PHONE_NUMBER_REG).matcher(mobile).matches();
    }
}
