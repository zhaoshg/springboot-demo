package com.hlgf.sms.mapper;

import com.hlgf.sms.model.BalanceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ccr12312@163.com at 2018-12-4
 */
@Mapper
@Component
public interface BalanceMapper {

    /**
     * 获取客户余额
     * @return List<BalanceEntity>
     */
    List<BalanceEntity> findBalance();

}
