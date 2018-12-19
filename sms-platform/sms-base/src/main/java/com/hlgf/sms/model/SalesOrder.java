package com.hlgf.sms.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author ccr12312@163.com at 2018-11-7
 */
@Setter
@Getter
public class SalesOrder implements SmsLogCapable {

    public SalesOrder(String customerName, String mobile, String pkCustomer, String pkGroup, String groupName, String pkRelated, String relatedNum, String passDate, Integer state) {
        this.customerName = customerName;
        this.mobile = mobile;
        this.pkCustomer = pkCustomer;
        this.pkGroup = pkGroup;
        this.groupName = groupName;
        this.pkRelated = pkRelated;
        this.relatedNum = relatedNum;
        this.passDate = passDate;
        this.state = state;
    }

    private String customerName;

    private String mobile;

    private String pkCustomer;

    private String pkGroup;

    private String groupName;

    private String pkRelated;

    private String relatedNum;

    private String passDate;

    private Integer state;

    private BigDecimal totalAmount;

    private List<SalesOrderItem> itemList;

    private String openId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalesOrder that = (SalesOrder) o;
        return Objects.equals(pkRelated, that.pkRelated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkRelated);
    }

    @Override
    public SmsSendLog toSmsSendLog() {
        SmsSendLog log = new SmsSendLog();
        log.setReceiverName(this.getCustomerName());
        log.setReceiverNumber(this.getMobile());
        log.setPkReceiver(this.getPkCustomer());
        log.setPkGroup(this.getPkGroup());
        log.setGroupName(this.getGroupName());
        log.setRelatedId(this.getPkRelated());
        log.setRelatedOrder(this.getRelatedNum());
        return log;
    }
}
