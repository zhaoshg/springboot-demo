package com.hlgf.sms.service.impl;

import com.hlgf.sms.TestTemplate;
import com.hlgf.sms.model.BalanceEntity;
import com.hlgf.sms.service.IBalanceService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author ccr12312@163.com at 2018-12-7
 */
public class BalanceServiceImplTest extends TestTemplate {

    @Autowired
    IBalanceService balanceService;

    @Test
    public void findBalance() {
        List<BalanceEntity> list = balanceService.findBalance();
        Assert.assertEquals(0,list.size());
    }
}