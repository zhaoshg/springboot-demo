package com.hlgf.sms.service;

import com.hlgf.sms.model.SmsSendLog;

import java.util.Map;

/**
 * 消息发送接口
 *
 * @author ccr12312@163.com at 2018-10-29
 */
public interface MsgSender {

    /**
     * 发送短信接口
     *
     * @return 0 成功，非0 失败状态码
     */
    void send(SmsSendLog sms);


    /**
     * 发送短信 。 插入log . 要拆分一下。最多50个一组
     *
     * @param smsMap
     */
    public void betchSend(Map<String, SmsSendLog> smsMap);

}
