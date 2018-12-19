package com.hlgf.sms.scheduler;

import com.hlgf.sms.Constants;
import com.hlgf.sms.model.FundsArrivalsEntity;
import com.hlgf.sms.model.SmsSendLog;
import com.hlgf.sms.service.FundsArrivalsService;
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

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author ccr12312@163.com at 2018-11-5
 */
@Log4j2
@Data
@Component
public class FundsArrivalsNotificationTask {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String className = FundsArrivalsNotificationTask.class.getSimpleName();

    //当查询数据过多时，分批处理，每批处理个数
    private static final int excutorSize = 500;
    //每30分钟扫描发送
//    private static final String CRON = "0 0/30 * * * ? ";
    private static final String CRON = "0 0/1 * * * ? ";

    private static String PHONE_NUMBER_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

    @Value("${sms.template.funds-arrivals}")
    private String template;

    @Autowired
    private FundsArrivalsService fundsArrivalsService;

    @Autowired
    private SmsSendLogService logService;

    @Autowired
    private MsgSender msgSender;


    @Resource(name = "fundsArrivalsWxSender")
    private TemplateSender templateSender;

    @Scheduled(cron = CRON)
    public void sendSMS() {
        long nano = System.currentTimeMillis();
        log.info("========{} job start scheduling at {}", this.getClass().getSimpleName(), dateFormat.format(new Date()));
        String lastSendTime = logService.findLastInsertTime(Constants.SmsType.SMS_TYPE_FUNDS_ARRIVALS_NOTIFICATION);
        List<FundsArrivalsEntity> fundsArrivalsList = fundsArrivalsService.findFundsArrivalsPassedRecently(lastSendTime);
        if (fundsArrivalsList == null)
            fundsArrivalsList = new ArrayList<>();
        log.info("loaded List after {},loaded {} records. cost {} MS", lastSendTime, fundsArrivalsList.size(), (System.currentTimeMillis() - nano));

        int size = fundsArrivalsList.size();
        if (size > 0) {
            if (size > excutorSize) {
                int part = size / excutorSize;//分批数
                log.info("{} job total Data : {} ，Split into {} executions ", className, size, part);
                for (int i = 0; i < part; i++) {
                    List listPage = fundsArrivalsList.subList(0, excutorSize);
                    long start = System.currentTimeMillis();
                    log.info("{} job start the {} execution", className, (i + 1));
                    executor(listPage);
                    long end = System.currentTimeMillis();
                    log.info("{} job end the {} execution , cost {} MS", className, (i + 1), (end - start));
                    fundsArrivalsList.subList(0, excutorSize).clear();
                }
            }
            if (!fundsArrivalsList.isEmpty()) {
                long start = System.currentTimeMillis();
                log.info("{} job start the last execution", className);
                executor(fundsArrivalsList);
                long end = System.currentTimeMillis();
                log.info("{} job end the last execution , cost {} MS", className, (end - start));
            }
        }
        log.info("========{} job done scheduling at {} with {}ms", className, dateFormat.format(new Date()), System.currentTimeMillis() - nano);
    }

    /**
     * 处理获取的数据
     *
     * @param fundsArrivalsList
     */
    private void executor(List<FundsArrivalsEntity> fundsArrivalsList) {
        long time0 = System.currentTimeMillis();
        //批量查询smsSendLog,判断是否已经发送过
        Map<String, Integer> cntMap = new HashMap();
        List relatedList = new ArrayList();
        fundsArrivalsList.forEach(x -> {
            relatedList.add(x.getPkRelated());
        });
        List<SmsSendLog> logList = logService.findList(null, relatedList, Constants.SmsType.SMS_TYPE_FUNDS_ARRIVALS_NOTIFICATION, null);
        logList.forEach(x -> {
            if (cntMap.containsKey(x.getRelatedId())) {
                cntMap.put(x.getRelatedId(), 1);
            }
        });
        Map<String, SmsSendLog> smsMap = new HashMap<>();
        List<FundsArrivalsEntity> wxList = new ArrayList<>();
        fundsArrivalsList.stream().filter(x -> {
            if (!validateMobile(x.getMobile())) {
                log.error("{} invalid mobile number {},receiver is {} , related PK {} ignored.", className, x.getMobile(), x.getCustomerName(), x.getPkRelated());
                return false;
            } else {
                //不允许重复发送
                if (cntMap.containsKey(x.getPkRelated())) {
                    log.error("{} target mobile {}, related pk {} has already send.", className, x.getMobile(), x.getPkRelated());
                    return false;
                }
                if (WxConstants.getBindInfo().containsKey(x.getMobile())) {
                    x.setOpenId(WxConstants.getBindInfo().get(x.getMobile()));
                    wxList.add(x);
                }
                return true;
            }
        }).forEach(x -> {

            String content = String.format(template, x.getGroupName(), FormatUtil.transferDateToExactFormat(x.getPassDate()), FormatUtil.formatMoney(x.getMoneyCr()), x.getGroupNumber() == null ? "0551-62667301" : x.getGroupNumber());
            SmsSendLog sms = x.toSmsSendLog();
            sms.setContent(content);
            sms.setSmsType(Constants.SmsType.SMS_TYPE_FUNDS_ARRIVALS_NOTIFICATION);
            sms.setSendTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            sms.setWordCount(sms.getContent().length());
            sms.setSendCnt(1);
            smsMap.put(sms.getRelatedId(), sms);
            //FIXME for test
            wxList.add(x);
        });

        long time1 = System.currentTimeMillis();
        log.debug("{} job's executor prepared for Sms end , cost {} ms", className, (time1 - time0));
        log.debug("{} job's executor send Sms start ", className);

        msgSender.betchSend(smsMap);
        for (FundsArrivalsEntity entity : wxList) {
            templateSender.sendWxMsg(entity);
        }
        smsMap.entrySet().forEach(x -> {
            SmsSendLog sms = x.getValue();
            log.info("{} job's executor send Sms {} , receiver is {} ,mobile is {}, content is {} ", className, sms.getSendState().equals("Y") ? "success" : "failure", sms.getReceiverName(), sms.getReceiverNumber(), sms.getContent());
        });
        log.info("{} job's executor send Sms cost {} ms", className, (System.currentTimeMillis() - time1));
    }

    private boolean validateMobile(String mobile) {
        return null != mobile && !mobile.isEmpty() && Pattern.compile(PHONE_NUMBER_REG).matcher(mobile).matches();
    }
}
