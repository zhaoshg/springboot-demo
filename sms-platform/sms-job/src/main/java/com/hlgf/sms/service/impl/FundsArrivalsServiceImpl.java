package com.hlgf.sms.service.impl;

import com.hlgf.sms.mapper.FundsArrivalsMapper;
import com.hlgf.sms.model.FundsArrivalsEntity;
import com.hlgf.sms.service.FundsArrivalsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * @author ccr12312@163.com at 2018-11-5
 */
@Log4j2
@Service
public class FundsArrivalsServiceImpl implements FundsArrivalsService {

    @Autowired
    private FundsArrivalsMapper fundsArrivalsMapper;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<FundsArrivalsEntity> findFundsArrivalsPassedRecently(String timeLimit) {
        return fundsArrivalsMapper.findFundsArrivalsPassedRecently(getDefaultTimeStrOnIllegalTimePattern(timeLimit));
    }

    private String getDefaultTimeStrOnIllegalTimePattern(String timeLimit) {
        if (log.isInfoEnabled()) {
            log.info("validating time pattern,time limit is {}", timeLimit);
        }
        String date = timeLimit;
        //timeLimit为空
        if (!(timeLimit != null && !timeLimit.isEmpty())) {
            date = LocalDateTime.now().minusMinutes(30).format(formatter);
        } else {
            try {
                //30分钟以前
                LocalDateTime thirtyMinAgo = LocalDateTime.now().minusMinutes(30);
                //如果在30分钟以内
                if (LocalDateTime.parse(timeLimit, formatter).isAfter(thirtyMinAgo)) {
                    date = thirtyMinAgo.format(formatter);
                }
            } catch (DateTimeParseException e) {
                //timeLimit非法时间格式
                date = LocalDateTime.now().minusMinutes(30).format(formatter);
            }
        }
        return date;
    }
}
