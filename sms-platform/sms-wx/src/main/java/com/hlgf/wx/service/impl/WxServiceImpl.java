package com.hlgf.wx.service.impl;

import com.hlgf.sms.mapper.WxBindMapper;
import com.hlgf.sms.model.WxBindEntity;
import com.hlgf.wx.model.AccessToken;
import com.hlgf.wx.model.WxUserInfo;
import com.hlgf.wx.service.WxService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
@Service
public class WxServiceImpl implements WxService {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WxBindMapper wxBindMapper;


    //通过code换取网页授权access_token的链接
    @Value("${wx.url.getAccessToken}")
    private String getAccessToken;

    //通过code换取网页授权access_token的链接
    @Value("${wx.url.getWebaccessToken}")
    private String getWebAccessToken;

    //通过code换取网页授权access_token的链接
    @Value("${wx.url.refreshAccessToken}")
    private String refreshAccessTokenUrl;


    //拉取用户信息url
    @Value("${wx.url.getUserInfo}")
    private String getUserInfoUrl;

    @Override
    public AccessToken getWebAccessToken(String code) {
        String url = String.format(getWebAccessToken, code);
        AccessToken token = restTemplate.getForObject(url, AccessToken.class);
        return token;
    }

    @Override
    public AccessToken refreshWebAccessToken(String refreshToken) {
        String url = String.format(refreshAccessTokenUrl, refreshToken);
        AccessToken token = restTemplate.getForObject(url, AccessToken.class);
        return token;
    }

    @Override
    public WxUserInfo getUserInfo(String accessToken, String openId) {
        String url = String.format(getUserInfoUrl, accessToken, openId);
        WxUserInfo userInfo = restTemplate.getForObject(url, WxUserInfo.class);

        return userInfo;
    }

    @Override
    public boolean bindWX(WxBindEntity bindEntity) {
        if (bindEntity == null || bindEntity.getOpenId() == null || bindEntity.getPhone() == null)
            return false;
        WxBindEntity old = wxBindMapper.getBindByPhone(bindEntity.getPhone());
        if (old != null) {
            old.setUpdateTime(LocalDateTime.now().format(formatter));
            old.setNickName(bindEntity.getNickName());
            old.setOpenId(bindEntity.getOpenId());
            old.setRemark("udpate");
            wxBindMapper.updateBindInfo(old);
        } else {
            bindEntity.setBindTime(LocalDateTime.now().format(formatter));
            bindEntity.setUpdateTime(LocalDateTime.now().format(formatter));
            bindEntity.setState(1);
            bindEntity.setRemark("");
            int cnt = wxBindMapper.insertLog(bindEntity);
            if (cnt != 1)
                return false;
        }
        return true;
    }

    @Override
    public WxBindEntity getBindInfo(String openId) {
        return wxBindMapper.getBindByOpenId(openId);
    }
}
