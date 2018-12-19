package com.hlgf.sms.service.impl;

import com.hlgf.sms.mapper.SmsSendLogMapper;
import com.hlgf.sms.service.Sender;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ccr12312@163.com at 2018-10-29
 */
@Service("CUCCSender")
@Log4j2
@Data
public class CUCCSenderImpl implements Sender {


    @Value("${sender.CUCC.singleUrl}")
    private String singleUrl;

    @Value("${sender.CUCC.batchUrl}")
    private String batchUrl;

    @Value("${sender.CUCC.epid}")
    private String epid;

    @Value("${sender.CUCC.User_Name}")
    private String User_Name;

    @Value("${sender.CUCC.password}")
    private String password;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SmsSendLogMapper smsSendLogMapper;

    @Override
    public boolean smsSend(String content, String number, String msgId) {
        Map<String, String> params = new HashMap<>();
        params.put("op", "SendSms");
        params.put("epid", epid);
        params.put("User_Name", User_Name);
        params.put("password", password);
        params.put("phone", number);
        params.put("content", content);
        params.put("MsgID", msgId);
        String response = null;
        boolean result = false;
        //  执行HTTP请求
        try {
            response = restTemplate.getForObject(singleUrl, String.class, params);
            //  输出结果
            log.info("send SMS return :{}", response);
            if (response.contains(ErrorCode.code_00.getCode())) {
                result = Boolean.TRUE;
            } else {
                for (ErrorCode value : ErrorCode.values()) {
                    if (response.contains(value.getCode())) {
                        log.error("短信发送失败,{}", value.getDescription());
                    }
                }
            }
        } catch (
                Exception e) {
            log.error("短信发送失败，{}", e);
        }
        return result;
    }


    enum ErrorCode {
        code_00(">00<", "发送成功"),
        code_01(">01<", "号码（超过上限50个）、内容等为空或内容长度超过300"),
        code_02(">02<", "用户鉴权失败"),
        code_03(">03<", "登录IP黑名单"),
        code_10(">10<", "余额不足（仅针对公免账户）"),
        code_99(">99<", "服务器接受失败"),
        code_other("other", "内容有屏蔽字");

        ErrorCode(String code, String description) {
            this.code = code;
            this.description = description;
        }

        private String code;
        private String description;

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
}
