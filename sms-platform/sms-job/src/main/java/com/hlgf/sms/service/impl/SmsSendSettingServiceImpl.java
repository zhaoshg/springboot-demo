package com.hlgf.sms.service.impl;

import com.hlgf.sms.mapper.SmsSendSettingMapper;
import com.hlgf.sms.model.SmsSendSetting;
import com.hlgf.sms.service.SmsSendSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ccr12312@163.com at 2018-11-5
 */
@Slf4j
@Service
public class SmsSendSettingServiceImpl implements SmsSendSettingService {

    @Autowired
    SmsSendSettingMapper mapper;

    @Override
    @Transactional
    public int addSetting(SmsSendSetting sendSetting) {
        log.info("add settings-{},{},{}", sendSetting.getPkGroup(), sendSetting.getGroupName(), sendSetting.getState());
        int updated;
        if ((updated = mapper.updateSetting(sendSetting.getPkGroup(), sendSetting.getState())) < 1) {
            return mapper.insertSetting(sendSetting);
        }
        return updated;
    }

    @Override
    @Transactional
    public int deleteSetting(String pkGroup, int state) {
        Integer s = mapper.findStateByGroup(pkGroup);
        if ((s != null) && (s & state) != 0) {
            log.info("delete settings-{},{}", pkGroup, state);
            return mapper.updateSetting(pkGroup, s ^ state);
        } else
            log.info("no need deleting settings-{},{}", pkGroup, state);
        return 0;

    }

    @Override
    public boolean checkWorkListRemind(String pkGroup) {
        return checkState(pkGroup, SmsSendSetting.WORK_LIST_REMIND);
    }

    @Override
    public boolean checkFundsArrivalsNotification(String pkGroup) {
        return checkState(pkGroup, SmsSendSetting.FUNDS_ARRIVALS_NOTIFICATION);
    }

    @Override
    public boolean checkSalesOrderNotification(String pkGroup) {
        return checkState(pkGroup, SmsSendSetting.SALES_ORDER_NOTIFICATION);
    }

    @Override
    public boolean checkBalanceRemind(String pkGroup) {
        return checkState(pkGroup, SmsSendSetting.BALANCE_REMIND);
    }

    @Override
    public List<SmsSendSetting> getAll() {
        return mapper.getAll();
    }

    @Override
    public boolean updateSetting(String pkGroup, int state) {
        return mapper.updateSetting(pkGroup, state) == 1;
    }

    private boolean checkState(String pkGroup, int state) {
        Integer s = mapper.findStateByGroup(pkGroup);
        s = (s == null ? 0 : s);
        return (s & state) != 0;
    }
}
