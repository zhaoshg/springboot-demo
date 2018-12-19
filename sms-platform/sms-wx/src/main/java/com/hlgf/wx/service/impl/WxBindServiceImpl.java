package com.hlgf.wx.service.impl;

import com.hlgf.sms.model.WxBindEntity;
import com.hlgf.wx.service.WxBindService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccr12312@163.com at 2018-10-29
 */
@Slf4j
@Service
public class WxBindServiceImpl implements WxBindService {


    @Override
    public String insertLog(WxBindEntity entity) {
        return null;
    }

    @Override
    public int batchInsertLog(List<WxBindEntity> list) {
        return 0;
    }

    @Override
    public WxBindEntity getBindByOpenId(String openId) {
        return null;
    }
}