package com.hlgf.sms;

import com.hlgf.sms.actable.AutoCreateTable;
import com.hlgf.sms.listener.ApplicationEnvironmentPreparedEventListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.hlgf.sms.scheduler", "com.hlgf.sms.controller", "com.hlgf.sms.mapper", "com.hlgf.sms.model", "com.hlgf.sms.service", "com.hlgf.wx.service", "com.hlgf.wx.message", "com.hlgf.wx"})
@SpringBootApplication(exclude = {JpaRepositoriesAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class SmsJobApplication {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("actable")) {
            //在自动建表时，禁用自动重启
            System.setProperty("spring.devtools.restart.enabled", "false");
            SpringApplication app = new SpringApplication(AutoCreateTable.class);
            app.addListeners(new ApplicationEnvironmentPreparedEventListener());
            String[] dst = new String[]{"--spring.config.location=classpath:/actable.properties"};
            app.run(dst);
        } else {
            SpringApplication.run(SmsJobApplication.class, args);
        }
    }
}