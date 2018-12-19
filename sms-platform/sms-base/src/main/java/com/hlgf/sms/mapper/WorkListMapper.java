package com.hlgf.sms.mapper;

import com.hlgf.sms.model.WorkListEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 工作任务
 *
 * @author ccr12312@163.com at 2018-10-25
 */
@Mapper
@Component("workListMapper")
public interface WorkListMapper {
    /**
     * 返回最近未未处理的工作任务
     * 短信发送间隔30min，最多发送3次
     *
     * @param timeLimit 最近的时间限制 format:yyyy-MM-dd HH:mm:ss
     * @return 工作任务列表
     */
    List<WorkListEntity> findWorkListUnhandledRecently(@Param("timeLimit") String timeLimit, @Param("timeDeadline") String timeDeadline, @Param("smsType") String smsType);
}
