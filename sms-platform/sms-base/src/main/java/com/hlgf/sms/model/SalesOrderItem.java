package com.hlgf.sms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author ccr12312@163.com at 2018-11-7
 */
@Setter
@Getter
@AllArgsConstructor
public class SalesOrderItem {

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

    private BigDecimal amount;
}
