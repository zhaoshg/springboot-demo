package com.hlgf.sms.actable;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.context.annotation.Configuration;


/**
 * 自动建表
 *
 * @author ccr12312@163.com at 2018-10-25
 */
@Configuration
@EnableAutoConfiguration(exclude = {MybatisAutoConfiguration.class, BatchAutoConfiguration.class, JmxAutoConfiguration.class, SpringApplicationAdminJmxAutoConfiguration.class})
//@ComponentScan(basePackages={"com.hlgf.sms.actable","com.hlgf.sms.model"})
@EntityScan("com.hlgf.sms.model")
public class AutoCreateTable {
//
//    public static void main(String[] args) {
//        SpringApplication app = new SpringApplication(AutoCreateTable.class);
//        app.addListeners(new ApplicationEnvironmentPreparedEventListener());
//        String[] dst = new String[]{"--spring.config.location=classpath:/actable.properties"};
//        app.run(dst);
//    }

}
