package com.hlgf.wx.service;

import com.hlgf.sms.model.WxBindEntity;
import com.hlgf.wx.model.TemplateMessage;
import com.hlgf.wx.model.WxResponse;
import com.hlgf.wx.model.AccessToken;
import com.hlgf.wx.model.WxUserInfo;

public interface WxService {

    /**
     * 通过code换取网页授权access_token，这时也可以获得openid
     *
     * @param code 授权码
     * @return
     */
    AccessToken getWebAccessToken(String code);


    /**
     * 由于access_token拥有较短的有效期，当access_token超时后，
     * 可以使用refresh_token进行刷新，refresh_token有效期为30天，
     * 当refresh_token失效之后，需要用户重新授权。
     *
     * @param refreshToken 之前通过getWebAccessToken获取到的refresh_token参数
     * @return
     */
    AccessToken refreshWebAccessToken(String refreshToken);

    /**
     * 获取到微信个人用户的信息
     *
     * @param accessToken
     * @param openId
     * @return
     */
    WxUserInfo getUserInfo(String accessToken, String openId);


    boolean bindWX(WxBindEntity bindEntity);

    WxBindEntity getBindInfo(String openId);
}
