package com.hlgf.sms.model;

/**
 * 将各种业务实体转化为日志实例
 *
 * @author ccr12312@163.com at 2018-11-6
 */
public interface SmsLogCapable {

    SmsSendLog toSmsSendLog();
}
