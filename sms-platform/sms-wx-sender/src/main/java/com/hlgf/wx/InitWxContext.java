package com.hlgf.wx;

import com.hlgf.wx.service.WxSenderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 用来初始化系统的AccessToken，以便每次启动项目都会获取最新的token。
 * 后续的更新就交给定时任务。
 */
@Log4j2
@Component
@Order(value = 1)
public class InitWxContext implements ApplicationRunner {
    @Autowired
    private WxSenderService wxSenderService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //TODO  Commented Out for test
//              wxSenderService.initWxContext();
    }
}
