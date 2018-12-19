package com.hlgf.wx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@MapperScan(basePackages = {"com.hlgf.sms.mapper", "com.hlgf.sms.model"})
@ComponentScan(basePackages = {"com.hlgf.wx", "com.hlgf.wx.controller", "com.hlgf.sms.mapper", "com.hlgf.sms.model", "com.hlgf.wx.model", "com.hlgf.sms.service", "com.hlgf.wx.service"})
public class WeixinSMSApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeixinSMSApplication.class, args);
    }
}