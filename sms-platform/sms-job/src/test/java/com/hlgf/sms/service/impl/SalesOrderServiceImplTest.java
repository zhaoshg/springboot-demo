package com.hlgf.sms.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hlgf.sms.TestTemplate;
import com.hlgf.sms.service.SalesOrderService;
import com.hlgf.sms.model.SalesOrder;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * @author ccr12312@163.com at 2018-11-7
 */
@Log4j2
public class SalesOrderServiceImplTest extends TestTemplate {

    @Autowired
    SalesOrderService service;

    @Test
    public void findSalesOrderPassedRecently() throws IOException {
        List<SalesOrder> orders = service.findSalesOrderPassedRecently("2018-04-12 15:04:54");

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(orders));
    }
}