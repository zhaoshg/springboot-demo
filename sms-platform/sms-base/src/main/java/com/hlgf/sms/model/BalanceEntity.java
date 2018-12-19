package com.hlgf.sms.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * 余额提醒
 * @author ccr12312@163.com at 2018-12-4
 */
@Setter
@Getter
public class BalanceEntity  implements SmsLogCapable{

    private static Pattern PHONE_NUMBER_REG_PATTERN = Pattern.compile("^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$");


    /**
     * 客户名称
     */
    private String customerName;

    private String tel1;

    private String tel2;

    /**
     * 客户手机号是tel1，tel2，tel3三者之一
     */
    private String tel3;

    /**
     * 客户表主键
     */
    private String pkCustomer;

    /**
     * 企业Id
     */
    private String pkGroup;

    /**
     * 企业名称
     */
    private String groupName;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 企业电话
     */
    private String groupNumber;

    private String openId;

    public String getMobile() {
        if (validateMobile(tel1)) {
            return tel1;
        } else if (validateMobile(tel2)) {
            return tel2;
        } else if (validateMobile(tel3)) {
            return tel3;
        } else {
            return "";
        }
    }

    private boolean validateMobile(String mobile) {
        return null != mobile && !mobile.isEmpty() && PHONE_NUMBER_REG_PATTERN.matcher(mobile).matches();
    }

    @Override
    public SmsSendLog toSmsSendLog() {
        SmsSendLog log = new SmsSendLog();
        log.setReceiverName(this.getCustomerName());
        log.setReceiverNumber(this.getMobile());
        log.setPkReceiver(this.getPkCustomer());
        log.setPkGroup(this.getPkGroup());
        log.setGroupName(this.getGroupName());
        return log;
    }
}
