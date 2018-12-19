package com.hlgf.sms.service.impl;

import com.hlgf.sms.mapper.SalesOrderMapper;
import com.hlgf.sms.model.SalesOrderEntity;
import com.hlgf.sms.service.SalesOrderService;
import com.hlgf.sms.model.SalesOrder;
import com.hlgf.sms.model.SalesOrderItem;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ccr12312@163.com at 2018-11-7
 */
@Log4j2
@Service
public class SalesOrderServiceImpl implements SalesOrderService {

    @Autowired
    SalesOrderMapper salesOrderMapper;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<SalesOrder> findSalesOrderPassedRecently(String timeLimit) {
        List<SalesOrderEntity> entityList = salesOrderMapper.findSalesOrderPassedRecently(getDefaultTimeStrOnIllegalTimePattern(timeLimit));
        //分组聚合
        Map<SalesOrder, List<SalesOrderItem>> groupBy = entityList.stream().collect(Collectors.groupingBy(
                x -> new SalesOrder(x.getCustomerName(), x.getMobile(), x.getPkCustomer(), x.getPkGroup(),
                        x.getGroupName(), x.getPkRelated(), x.getRelatedNum(), x.getPassDate(), x.getState()),
                Collectors.mapping(x -> new SalesOrderItem(x.getProductName(), x.getProductSize(),
                        x.getCount(), x.getPrice(), x.getUnit(), Optional.ofNullable(x.getCount()).orElse(BigDecimal.ZERO).multiply(Optional.ofNullable(x.getPrice()).orElse(BigDecimal.ZERO))), Collectors.toList())));
        List<SalesOrder> salesOrderList = new ArrayList<>();
        for (Map.Entry<SalesOrder, List<SalesOrderItem>> entry : groupBy.entrySet()) {
            SalesOrder k = entry.getKey();
            List<SalesOrderItem> v = entry.getValue();
            k.setItemList(v);
            k.setTotalAmount(v.stream().map(SalesOrderItem::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            salesOrderList.add(k);
        }
        return salesOrderList;
    }

    private String getDefaultTimeStrOnIllegalTimePattern(String timeLimit) {
        if (log.isInfoEnabled()) {
            log.info("validating time pattern,time limit is {}", timeLimit);
        }
        String date = timeLimit;
        //timeLimit为空
        if (!(timeLimit != null && !timeLimit.isEmpty())) {
            date = LocalDateTime.now().minusHours(1).format(formatter);
        } else {
            try {
                //60分钟以前
                LocalDateTime anHourAgo = LocalDateTime.now().minusHours(1);
                //如果在60分钟以内
                if (LocalDateTime.parse(timeLimit, formatter).isAfter(anHourAgo)) {
                    date = anHourAgo.format(formatter);
                }
            } catch (DateTimeParseException e) {
                //timeLimit非法时间格式
                date = LocalDateTime.now().minusHours(1).format(formatter);
            }
        }
        return date;
    }
}
