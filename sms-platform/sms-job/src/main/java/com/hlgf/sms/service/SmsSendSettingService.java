package com.hlgf.sms.service;

import com.hlgf.sms.model.SmsSendSetting;

import java.util.List;

/**
 * @author ccr12312@163.com at 2018-11-5
 */
public interface SmsSendSettingService {

    int addSetting(SmsSendSetting sendSetting);

    int deleteSetting(String pkGroup, int state);

    /**
     * 查看公司是否配置了工作任务提醒
     */
    boolean checkWorkListRemind(String pkGroup);

    /**
     * 检查公司是否配置了到款通知
     */
    boolean checkFundsArrivalsNotification(String pkGroup);

    /**
     * 检查公司是否配置了销售单通知
     */
    boolean checkSalesOrderNotification(String pkGroup);

    /**
     * 检查公司是否配置了余额提醒
     */
    boolean checkBalanceRemind(String pkGroup);

    List<SmsSendSetting> getAll();

    boolean updateSetting(String pkGroup,int state);

}
