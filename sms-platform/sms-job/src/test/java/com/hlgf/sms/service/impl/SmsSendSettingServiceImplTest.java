package com.hlgf.sms.service.impl;

import com.hlgf.sms.TestTemplate;
import com.hlgf.sms.model.SmsSendSetting;
import com.hlgf.sms.service.SmsSendSettingService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ccr12312@163.com at 2018-11-5
 */
public class SmsSendSettingServiceImplTest extends TestTemplate {

    @Autowired
    SmsSendSettingService smsSendSettingService;

    @Test
    public void addSetting() {
        SmsSendSetting setting = new SmsSendSetting();
        setting.setPkGroup("0001T110000000001ET2");
        setting.setGroupName("安徽辉隆集团农资连锁有限责任公司");
        setting.setState(SmsSendSetting.WORK_LIST_REMIND | SmsSendSetting.FUNDS_ARRIVALS_NOTIFICATION | SmsSendSetting.BALANCE_REMIND);
        Assert.assertEquals(1, smsSendSettingService.addSetting(setting));
    }

    @Test
    public void deleteSettingAbsent() {
        Assert.assertEquals(0, smsSendSettingService.deleteSetting("0001T110000000001ET2", SmsSendSetting.SALES_ORDER_NOTIFICATION));
    }

    @Test
    public void deleteSettingNormal() {
        Assert.assertEquals(1, smsSendSettingService.deleteSetting("0001T110000000001ET2", SmsSendSetting.WORK_LIST_REMIND));
    }

    @Test
    public void checkWorkListRemind() {
        Assert.assertFalse(smsSendSettingService.checkWorkListRemind("0001T110000000001ET2"));
    }

    @Test
    public void checkFundsArrivalsNotification() {
        Assert.assertTrue(smsSendSettingService.checkFundsArrivalsNotification("0001T110000000001ET2"));
    }

    @Test
    public void checkSalesOrderNotification() {
        Assert.assertFalse(smsSendSettingService.checkSalesOrderNotification("0001T110000000001ET2"));
    }

    @Test
    public void checkBalanceRemind() {
        Assert.assertTrue(smsSendSettingService.checkBalanceRemind("0001T110000000001ET2"));
    }
}