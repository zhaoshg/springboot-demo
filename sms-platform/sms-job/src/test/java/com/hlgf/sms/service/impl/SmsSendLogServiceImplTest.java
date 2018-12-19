package com.hlgf.sms.service.impl;

import com.hlgf.sms.Constants;
import com.hlgf.sms.TestTemplate;
import com.hlgf.sms.model.WorkListEntity;
import com.hlgf.sms.service.SmsSendLogService;
import com.hlgf.sms.service.WorkListService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @author ccr12312@163.com at 2018-10-29
 */
public class SmsSendLogServiceImplTest extends TestTemplate {

    @Autowired
    SmsSendLogService smsSendLogService;

    @Autowired
    WorkListService workListService;

    @Test
    public void testInsertLog() {
        List<WorkListEntity> workList = workListService.findWorkListUnhandledRecently(smsSendLogService.findLastInsertTime(Constants.SmsType.SMS_TYPE_WORK_LIST_REMIND), Constants.SmsType.SMS_TYPE_WORK_LIST_REMIND);
        workList.forEach(x -> {
            Assert.assertEquals(1, smsSendLogService.insertLog(x, String.format("工作任务短信-%s", x.getSubject()), Constants.SmsType.SMS_TYPE_WORK_LIST_REMIND, true));
        });
    }

    @Test
    public void testFindLastInsertTime() {
        Assert.assertSame(null, smsSendLogService.findLastInsertTime(Constants.SmsType.SMS_TYPE_WORK_LIST_REMIND));
    }

    @Test
    public void testFindCntByRelatedIdAndReceiver() {
        Assert.assertEquals(1, smsSendLogService.findList("18654151195", Arrays.asList("1001A1100000001CWBH4"), null, "2018-10-28 14:27:27").size());
    }
}