package com.hlgf.sms.scheduler;


import com.hlgf.sms.Constants;
import com.hlgf.wx.message.TemplateSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ccr12312@163.com at 2018-10-25
 */
@Log4j2
@Component
public class PreAlertScheduledTask extends NoticeSchedule {

    private static final String CRON = "0 0/1 * * * ? ";
//    private static final String CRON = "0 0,30 * * * ?";


    @Value("${sms.template.pre-alert}")
    private String template;


    @Resource(name = "prealertWxSender")
    private TemplateSender templateSender;

    @Scheduled(cron = CRON)
    public void sendSMS() {
        super.setTemplate(template);
        super.setTemplateSender(templateSender);
        super.setSmsType(Constants.SmsType.SMS_TYPE_PRE_ALERT);
        super.sendSMS();
    }
}