package com.hlgf.wx.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TODO Write down the function of this class
 *
 * @Description
 * @auther zhaoshg    mail:mr.zhaoshg@gmail.com
 * @create 2018-12-17 9:21
 */
public class FormatUtil {


    /**
     * 将 "yyyy-MM-dd HH:mm:ss"类型的日期转为"yyyy年MM月dd日HH时mm分"
     *
     * @param passDate
     * @return
     */
    public static String transferDateToExactFormat(String passDate) {
        DateTimeFormatter srcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dstFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分");
        return dstFormatter.format(LocalDateTime.parse(passDate, srcFormatter));
    }

    /**
     * 格式化金额
     *
     * @param moneyCr
     * @return
     */
    public static String formatMoney(BigDecimal moneyCr) {
        if (moneyCr == null)
            moneyCr = BigDecimal.ZERO;
        NumberFormat format = NumberFormat.getCurrencyInstance();
        return format.format(moneyCr);
    }
}
