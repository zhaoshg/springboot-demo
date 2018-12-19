package com.hlgf.wx.service;

import com.hlgf.wx.model.TemplateMessage;
import com.hlgf.wx.model.WxResponse;

public interface WxSenderService {
    /**
     * 获取微信接口授权token
     *
     * @return
     */
    String getAccessToken();


    /**
     * 发送微信模板消息
     *
     * @param accessToken
     * @param msg
     * @return
     */
    WxResponse sendTemplateMessage(String accessToken, TemplateMessage msg);

    void initWxContext();
}
