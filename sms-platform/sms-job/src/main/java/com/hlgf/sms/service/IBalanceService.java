package com.hlgf.sms.service;

import com.hlgf.sms.model.BalanceEntity;

import java.util.List;

/**
 * @author ccr12312@163.com at 2018-12-4
 */
public interface IBalanceService {

    List<BalanceEntity> findBalance();
}
