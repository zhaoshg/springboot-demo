package com.hlgf.sms.mapper;

import com.hlgf.sms.model.FundsArrivalsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ccr12312@163.com at 2018-11-5
 */

@Mapper
@Component
public interface FundsArrivalsMapper {

    /**
     * 扫描指定时间以后的所有已通过的到款
     *
     * @param timeLimit 指定时间
     * @return List<FundsArrivalsEntity>
     */
    List<FundsArrivalsEntity> findFundsArrivalsPassedRecently(@Param("timeLimit") String timeLimit);
}
