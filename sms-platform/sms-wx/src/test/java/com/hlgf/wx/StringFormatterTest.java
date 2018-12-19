package com.hlgf.wx;

import org.junit.Test;

import java.util.Date;

/**
 * @author ccr12312@163.com at 2018-11-8
 */
public class StringFormatterTest {

    @Test
    public void testDateFormatter() {
        System.out.printf("%1$tY年%1$tm月%1$td日%1$tH时%1$tM分", new Date());
    }

    @Test
    public void testDecimalFormatter() {
        System.out.printf("{0:C}", 0.6);
    }
}
