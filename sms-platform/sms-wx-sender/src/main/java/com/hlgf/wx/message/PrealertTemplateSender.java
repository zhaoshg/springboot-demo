package com.hlgf.wx.message;

import com.hlgf.sms.model.WorkListEntity;
import org.springframework.stereotype.Service;

@Service("prealertWxSender")
public class PrealertTemplateSender extends WorkListTemplateSender implements TemplateSender<WorkListEntity> {

    @Override
    public String sendWxMsg(WorkListEntity entity) {
        super.setTitle("你好，你有新的预警消息");
        return super.sendWxMsg(entity);
    }
}