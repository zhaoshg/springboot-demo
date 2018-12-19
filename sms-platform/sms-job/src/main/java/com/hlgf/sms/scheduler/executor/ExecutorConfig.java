package com.hlgf.sms.scheduler.executor;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author ccr12312@163.com at 2018-10-29
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.task.scheduling")
public class ExecutorConfig implements SchedulingConfigurer {

    private int corePoolSize = 1;

    @Bean("taskExecutor")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(corePoolSize);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }
}