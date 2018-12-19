package com.hlgf.sms.service;

import com.hlgf.sms.model.SmsLogCapable;
import com.hlgf.sms.model.SmsSendLog;

import java.util.List;

/**
 * 短信发送日志服务
 *
 * @author ccr12312@163.com at 2018-10-29
 */
public interface SmsSendLogService {

    /**
     * 保存发送日志
     *
     * @param content     发送内容
     * @param sendSuccess 是否发送成功
     */
    int insertLog(SmsLogCapable entity, String content, String smsType, boolean sendSuccess);

    /**
     * 批量保存发送日志
     */
    int batchInsertLog(List<SmsSendLog> list);

    /**
     * 最后一次插入时间
     */
    String findLastInsertTime(String logType);

    /**
     * 从 sendTime 开始的receiverNumber和relatedId的发送次数
     */
    List<SmsSendLog> findList(String receiverNumber, List<String> relatedIdList, String smsType, String sendTime);
}
