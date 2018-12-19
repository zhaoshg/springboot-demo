package com.hlgf.sms.service;

import com.hlgf.sms.model.SalesOrder;

import java.util.List;

/**
 * @author ccr12312@163.com at 2018-11-7
 */
public interface SalesOrderService {
    List<SalesOrder> findSalesOrderPassedRecently(String timeLimit);
}
