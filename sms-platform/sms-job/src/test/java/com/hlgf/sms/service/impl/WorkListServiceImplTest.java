package com.hlgf.sms.service.impl;

import com.hlgf.sms.Constants;
import com.hlgf.sms.TestTemplate;
import com.hlgf.sms.model.WorkListEntity;
import com.hlgf.sms.service.WorkListService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author ccr12312@163.com at 2018-10-29
 */
public class WorkListServiceImplTest extends TestTemplate {

    @Autowired
    WorkListService service;

    @Test
    public void testFindWorkListUnhandledRecentlyWithNullTimeLimit() {
        List<WorkListEntity> list = service.findWorkListUnhandledRecently(null, Constants.SmsType.SMS_TYPE_WORK_LIST_REMIND);
        Assert.assertSame(2, list.size());
    }

    @Test
    public void testFindWorkListUnhandledRecentlyWithIllegalTimeLimit() {
        List<WorkListEntity> list = service.findWorkListUnhandledRecently("aspss", Constants.SmsType.SMS_TYPE_WORK_LIST_REMIND);
        Assert.assertSame(2, list.size());
    }

    @Test
    public void testFindWorkListUnhandledRecentlyWithINormalTimeLimit() {
        List<WorkListEntity> list = service.findWorkListUnhandledRecently("2018-10-29 11:23:00", Constants.SmsType.SMS_TYPE_WORK_LIST_REMIND);
        Assert.assertSame(1, list.size());
    }

    @Test
    public void testGetDefaultTimeStrOnIllegalTimePatter() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String date = LocalDateTime.now().minusDays(1).format(formatter);
//        Assert.assertEquals(date,WorkListServiceImpl.getDefaultTimeStrOnIllegalTimePatter(""));
//        Assert.assertEquals(date,WorkListServiceImpl.getDefaultTimeStrOnIllegalTimePatter("abcd"));
    }
}