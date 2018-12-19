package com.hlgf.sms.mapper;

import com.hlgf.sms.model.SalesOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ccr12312@163.com at 2018-11-7
 */

@Mapper
@Component
public interface SalesOrderMapper {

    /**
     * 扫描指定时间以后的所有已通过的销售单
     *
     * @param timeLimit 指定时间
     * @return List<SalesOrderEntity>
     */
    List<SalesOrderEntity> findSalesOrderPassedRecently(@Param("timeLimit") String timeLimit);
}
