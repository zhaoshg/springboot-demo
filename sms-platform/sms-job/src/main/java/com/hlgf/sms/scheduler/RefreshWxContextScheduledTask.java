package com.hlgf.sms.scheduler;


import com.hlgf.wx.service.WxSenderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 微信接口的accessToken需要每小时刷新一次，防止过期。
 */
@Log4j2
@Component
public class RefreshWxContextScheduledTask {

    //每小时执行
//    private static final String CRON = "0 0/1 * * * ? ";
    private static final String CRON = "0 0 * ? * *";

    @Autowired
    private WxSenderService wxSenderService;


    @Scheduled(cron = CRON)
    public void init() {
        wxSenderService.initWxContext();
    }
}