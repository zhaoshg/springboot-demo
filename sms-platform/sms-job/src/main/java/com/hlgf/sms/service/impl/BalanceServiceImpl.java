package com.hlgf.sms.service.impl;

import com.hlgf.sms.mapper.BalanceMapper;
import com.hlgf.sms.model.BalanceEntity;
import com.hlgf.sms.service.IBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccr12312@163.com at 2018-12-4
 */
@Service
public class BalanceServiceImpl implements IBalanceService {

    @Autowired
    BalanceMapper balanceMapper;

    @Override
    public List<BalanceEntity> findBalance() {
        return balanceMapper.findBalance();
    }
}
