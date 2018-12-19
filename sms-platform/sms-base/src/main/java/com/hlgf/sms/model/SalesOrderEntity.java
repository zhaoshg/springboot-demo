package com.hlgf.sms.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * @author ccr12312@163.com at 2018-11-7
 */
@Setter
@Getter
public class SalesOrderEntity {

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
     * 销售单主键
     */
    private String pkRelated;

    /**
     * 销售单单号
     */
    private String relatedNum;

    /**
     * 审核通过时间
     */
    private String passDate;

    /**
     * 物料名称
     */
    private String productName;

    /**
     * 物料规格
     */
    private String productSize;

    /**
     * 采购数量
     */
    private BigDecimal count;

    /**
     * 价格
     */
    private BigDecimal price;

    private String unit;

    /**
     * 短信推送功能开通状态
     */
    private Integer state;

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
}
