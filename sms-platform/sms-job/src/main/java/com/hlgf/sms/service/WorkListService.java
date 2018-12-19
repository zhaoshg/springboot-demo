package com.hlgf.sms.service;

import com.hlgf.sms.model.WorkListEntity;

import java.util.List;

/**
 * @author ccr12312@163.com at 2018-10-25
 */
public interface WorkListService {
    /**
     * 扫描从timeLimit开始的所有未处理的工作任务
     */
    List<WorkListEntity> findWorkListUnhandledRecently(String timeLimit, String msgType);
}
