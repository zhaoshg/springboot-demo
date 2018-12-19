package com.hlgf.sms.service.impl;

import com.hlgf.sms.service.Sender;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author ccr12312@163.com at 2018-10-29
 */
@Service("CMCCSender")
@Log4j2
@Setter
@Getter
public class CMCCSenderImpl implements Sender {

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public boolean smsSend(String content, String number, String msgId) {
        return false;
    }
}
