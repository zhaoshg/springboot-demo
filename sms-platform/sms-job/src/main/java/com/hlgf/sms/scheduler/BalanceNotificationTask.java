package com.hlgf.sms.scheduler;

import com.hlgf.sms.Constants;
import com.hlgf.sms.model.BalanceEntity;
import com.hlgf.sms.model.SmsSendLog;
import com.hlgf.sms.service.IBalanceService;
import com.hlgf.sms.service.MsgSender;
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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 客户余额挺行
 *
 * @author ccr12312@163.com at 2018-11-7
 */
@Log4j2
@Data
@Component
public class BalanceNotificationTask {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String className = BalanceNotificationTask.class.getSimpleName();

    //当查询数据过多时，分批处理，每批处理个数
    private static final int excutorSize = 500;
    //每个季度第一天7点
    private static final String CRON = "0 0/1 * * * ? ";
//    private static final String CRON = "0 0 7 0 1,4,7,10 ? ";

    private static String PHONE_NUMBER_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

    @Value("${sms.template.balance}")
    private String template;


    @Autowired
    private IBalanceService balanceService;

    @Autowired
    private SmsSendLogService logService;

    @Autowired
    private MsgSender sender;

    @Resource(name = "balanceWxSender")
    private TemplateSender templateSender;

    @Scheduled(cron = CRON)
    public void sendSMS() {
        long nano = System.currentTimeMillis();
        log.info("========{} job start scheduling at {}", className, dateFormat.format(new Date()));
        List<BalanceEntity> balanceList = balanceService.findBalance();
        if (balanceList == null)
            balanceList = new ArrayList<>();
        long time1 = System.currentTimeMillis();
        log.info("{} job loaded List,loaded {} records , cost {} MS", className, balanceList.size(), (time1 - nano));

        int size = balanceList.size();
        if (size > 0) {
            if (size > excutorSize) {
                int part = size / excutorSize;//分批数
                log.info("{} job total Data : {} ，Split into {} executions ", className, size, part);
                for (int i = 0; i < part; i++) {
                    List listPage = balanceList.subList(0, excutorSize);
                    long start = System.currentTimeMillis();
                    log.info("{} job start the {} execution", className, (i + 1));
                    executor(listPage);
                    long end = System.currentTimeMillis();
                    log.info("{} job end the {} execution , cost {} MS", className, (i + 1), (end - start));
                    balanceList.subList(0, excutorSize).clear();
                }

            }
            if (!balanceList.isEmpty()) {
                long start = System.currentTimeMillis();
                log.info("{} job start the last execution", className);
                executor(balanceList);
                long end = System.currentTimeMillis();
                log.info("{} job end the last execution , cost {} MS", className, (end - start));
            }
        }
        log.info("========{} job done scheduling at {} with {}ms", className, dateFormat.format(new Date()), System.currentTimeMillis() - nano);
    }

    /**
     * 处理获取的数据
     *
     * @param balanceList
     */
    private void executor(List<BalanceEntity> balanceList) {
        long time0 = System.currentTimeMillis();
        log.info("{} job start to prepared data for Sms ", className);

        Map<String, SmsSendLog> smsMap = new HashMap<>();
        List<BalanceEntity> wxList = new ArrayList<>();
        balanceList.stream().filter(x -> {
            if (!validateMobile(x.getMobile())) {
                log.error("{} invalid mobile number {},receiver is {} , ignored.", className, x.getMobile(), x.getCustomerName());
                return false;
            }

            if (WxConstants.getBindInfo().containsKey(x.getMobile())) {
                x.setOpenId(WxConstants.getBindInfo().get(x.getMobile()));
                wxList.add(x);
                return false;
            }
            return true;

        }).forEach(x -> {
            //销售单详情
            String content = String.format(template, x.getGroupName(), x.getCustomerName(),
                    DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分").format(LocalDateTime.now()), FormatUtil.formatMoney(x.getBalance()), StringUtils.isEmpty(x.getGroupNumber()) ? "" : x.getGroupNumber());

            SmsSendLog sms = x.toSmsSendLog();
            sms.setContent(content);
            sms.setSmsType(Constants.SmsType.SMS_TYPE_BALANCE_REMIND);
            sms.setSendTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            sms.setWordCount(sms.getContent().length());
            sms.setSendCnt(1);
            sms.setRelatedId(x.getPkCustomer());
            smsMap.put(x.getPkCustomer(), sms);
            //FIXME for test
            wxList.add(x);
        });

        long time1 = System.currentTimeMillis();
        log.info("{} job prepared for Sms end , cost {} ms", className, (time1 - time0));
        log.info("{} job send Sms start ", className);

        sender.betchSend(smsMap);
        for (BalanceEntity entity : wxList) {
            templateSender.sendWxMsg(entity);
        }
        smsMap.forEach((key, sms) -> log.info("{} job's executor send Sms {} , receiver is {} ,mobile is {}, content is {}, ", className, sms.getSendState().equals("Y") ? "success" : "failure", sms.getReceiverName(), sms.getReceiverNumber(), sms.getContent()));
        long time2 = System.currentTimeMillis();
        log.info("{} job send Sms cost {} ms", className, (time2 - time1));
    }

    private boolean validateMobile(String mobile) {
        return null != mobile && !mobile.isEmpty() && Pattern.compile(PHONE_NUMBER_REG).matcher(mobile).matches();
    }
}