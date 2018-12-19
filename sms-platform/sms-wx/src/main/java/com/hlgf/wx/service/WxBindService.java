package com.hlgf.wx.service;

import com.hlgf.sms.model.WxBindEntity;

import java.util.List;

/**
 * 短信发送日志服务
 *
 * @author ccr12312@163.com at 2018-10-29
 */
public interface WxBindService {

    /**
     * 保存发送日志
     */
    String insertLog(WxBindEntity entity);

    /**
     * 批量保存发送日志
     */
    int batchInsertLog(List<WxBindEntity> list);


    WxBindEntity getBindByOpenId(String openId);
}
