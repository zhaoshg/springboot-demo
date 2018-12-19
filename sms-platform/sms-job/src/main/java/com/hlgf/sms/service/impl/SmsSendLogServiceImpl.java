package com.hlgf.sms.service.impl;

import com.hlgf.sms.mapper.SmsSendLogMapper;
import com.hlgf.sms.model.SmsLogCapable;
import com.hlgf.sms.model.SmsSendLog;
import com.hlgf.sms.service.SmsSendLogService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ccr12312@163.com at 2018-10-29
 */
@Log4j2
@Service
public class SmsSendLogServiceImpl implements SmsSendLogService {

    @Autowired
    private SmsSendLogMapper smsSendLogMapper;

    @Override
    @Transactional
    public int insertLog(SmsLogCapable entity, String content, String smsType, boolean sendSuccess) {
        if (entity == null) return 0;
        SmsSendLog log = entity.toSmsSendLog();
        log.setSendCnt(1);
        log.setSendState(sendSuccess ? "Y" : "N");
        log.setContent(content);
        log.setSmsType(smsType);
        log.setWordCount(log.getContent().length());
        log.setSendTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return smsSendLogMapper.insertLog(log);
    }

    @Override
    public int batchInsertLog(List<SmsSendLog> list) {
        if (null == list || list.isEmpty())
            return 0;
        return smsSendLogMapper.batchInsertLog(list);
    }

    @Override
    public String findLastInsertTime(String logtype) {
        return smsSendLogMapper.findLastInsertTime(logtype);
    }

    @Override
    public List<SmsSendLog> findList(String receiverNumber, List<String> relatedIdList, String smsType, String sendTime) {
        List<SmsSendLog> res = smsSendLogMapper.findList(relatedIdList, receiverNumber, smsType, sendTime);
        if (res == null) {
            res = new ArrayList<>();
        }
        return res;
    }
}
