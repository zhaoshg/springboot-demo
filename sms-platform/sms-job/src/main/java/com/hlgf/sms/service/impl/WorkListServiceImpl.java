package com.hlgf.sms.service.impl;

import com.hlgf.sms.mapper.WorkListMapper;
import com.hlgf.sms.model.WorkListEntity;
import com.hlgf.sms.service.WorkListService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * @author ccr12312@163.com at 2018-10-25
 */
@Log4j2
@Service
public class WorkListServiceImpl implements WorkListService {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    WorkListMapper workListMapper;

    /**
     * 返回最近未未处理的工作任务
     *
     * @return 工作任务列表
     */
    @Override
    public List<WorkListEntity> findWorkListUnhandledRecently(String timeLimit, String smsType) {
        return workListMapper.findWorkListUnhandledRecently(getDefaultTimeStrOnIllegalTimePattern(timeLimit), LocalDateTime.now().minusMinutes(30).format(formatter), smsType);
    }

    /**
     * 非法timeLimit时返回默认的时间
     * 如果timeLimit在两小时以内，则取之前刚好两个小时
     */
    private String getDefaultTimeStrOnIllegalTimePattern(String timeLimit) {
        if (log.isInfoEnabled()) {
            log.info("validating time pattern,time limit is {}", timeLimit);
        }
        String date = timeLimit;
        //timeLimit为空
        if (!(timeLimit != null && !timeLimit.isEmpty())) {
            date = LocalDateTime.now().minusDays(1).format(formatter);
        } else {
            try {
                //两小时以前
                LocalDateTime twoHoursAgo = LocalDateTime.now().minusHours(2);
                //如果在两个小时以内
                if (LocalDateTime.parse(timeLimit, formatter).isAfter(twoHoursAgo)) {
                    date = twoHoursAgo.format(formatter);
                }
            } catch (DateTimeParseException e) {
                //timeLimit非法时间格式
                date = LocalDateTime.now().minusDays(1).format(formatter);
            }
        }
        return date;
    }
}
