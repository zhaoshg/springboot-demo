package com.hlgf.wx.service.impl;

import com.hlgf.sms.mapper.WxBindMapper;
import com.hlgf.sms.model.WxBindEntity;
import com.hlgf.sms.service.Sender;
import com.hlgf.wx.WxConstants;
import com.hlgf.wx.model.AccessToken;
import com.hlgf.wx.model.TemplateMessage;
import com.hlgf.wx.model.WxResponse;
import com.hlgf.wx.service.WxSenderService;
import com.hlgf.wx.utils.JacksonUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class WxSenderServiceImpl implements WxSenderService {

    @Resource(name = "CUCCSender")
    private Sender sender;

    @Autowired
    private WxBindMapper wxBindMapper;

    @Autowired
    private RestTemplate restTemplate;

    //发送模板消息的链接
    @Value("${wx.url.sendTemplateMessage}")
    private String sendTemplateMessageUrl;


    //通过code换取网页授权access_token的链接
    @Value("${wx.url.getAccessToken}")
    private String getAccessToken;

    @Override
    public String getAccessToken() {
        AccessToken token = restTemplate.getForObject(getAccessToken, AccessToken.class);
        return token.getAccess_token();
    }

    @Override
    public WxResponse sendTemplateMessage(String accessToken, TemplateMessage msg) {
        WxResponse weixinResponse = new WxResponse();
        String url = String.format(sendTemplateMessageUrl, accessToken);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(JacksonUtil.objToJson(msg), headers);
        log.info("begin to send Weixin MSG :{} ", msg);
        //FIXME commented out for test
        weixinResponse = restTemplate.postForObject(url, formEntity, WxResponse.class);
        log.info("sendTemplateMessage return :{}", weixinResponse);
        return weixinResponse;
    }

    @Override
    public void initWxContext() {
        String token = getAccessToken();
        if (StringUtils.isEmpty(token)) {
            log.error("Couldn't get token from WX , token is null");
            sender.smsSend("Couldn't get token from WX , token is null", "18456539394", String.valueOf(System.currentTimeMillis()));
        }
        WxConstants.setAccessToken(token);
        log.info("init AccessToken:{}", token);
        List<WxBindEntity> bindInfos = wxBindMapper.getAll();
        Map<String, String> bindMap = new HashMap<>();

        for (WxBindEntity entity : bindInfos) {
            if (entity != null && !StringUtils.isEmpty(entity.getOpenId()) && !StringUtils.isEmpty(entity.getPhone())) {
                bindMap.put(entity.getPhone(), entity.getOpenId());
            }
        }

        log.info("init BindMap:{}", bindMap);
        WxConstants.setBindInfo(bindMap);
    }
}
