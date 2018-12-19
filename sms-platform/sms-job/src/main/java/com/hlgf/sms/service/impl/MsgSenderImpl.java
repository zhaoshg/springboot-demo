package com.hlgf.sms.service.impl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.hlgf.sms.mapper.SmsSendLogMapper;
import com.hlgf.sms.model.SmsSendLog;
import com.hlgf.sms.service.MsgSender;
import com.hlgf.sms.service.Sender;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@Data
public class MsgSenderImpl implements MsgSender {

    @Resource(name = "CUCCSender")
    private Sender sender;

    @Value("${sender.CUCC.excutorSize}")
    private static int excutorSize = 40;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SmsSendLogMapper smsSendLogMapper;

    @Override
    public void send(SmsSendLog sms) {
        singleSend(sms);
    }


    @Override
    public void betchSend(Map<String, SmsSendLog> smsMap) {
        log.info("========= start betch Send , message size = {} =========", smsMap.size());
        long nano = System.currentTimeMillis();
        List<SmsSendLog> logList = new ArrayList<>();
        smsMap.entrySet().stream().filter(x -> {
            return true;
        }).forEach(x -> {
            long start = System.currentTimeMillis();
            log.debug("start send message :{} ", x.getValue().getRelatedId());
            singleSend(x.getValue());
            long end = System.currentTimeMillis();
            log.debug("message {} sended {} , cost{} MS , recever is {} , content is {}", x.getValue().getRelatedId(), x.getValue().getSendState().equals("Y") ? "Success" : "Failure", (end - start), x.getValue().getReceiverNumber(), x.getValue().getContent());
            logList.add(x.getValue());
        });
        long start = System.currentTimeMillis();
        log.info("batchInsert logs start ");
        smsSendLogMapper.batchInsertLog(logList);
        log.debug("batchInsert logs end . cost {} MS", (System.currentTimeMillis() - start));

        log.info("========= ended betchSend , cost {} MS =========", (System.currentTimeMillis() - nano));
    }


    private void singleSend(SmsSendLog sms) {
        boolean res = sender.smsSend(sms.getContent(), sms.getReceiverNumber(), sms.getRelatedId());
        if (res)
            sms.setSendState("Y");
        else
            sms.setSendState("N");
    }


    @Getter
    @Setter
    @Data
    @JacksonXmlRootElement(localName = "SmsList")
    static
    class SmsContent {
        private List<MsgSenderImpl.SmsRow> row;
    }

    @Getter
    @Setter
    @Data
    static
    class SmsRow {
        @JacksonXmlProperty(isAttribute = true, localName = "phone")
        private String phone;
        @JacksonXmlProperty(isAttribute = true, localName = "info")
        @JacksonXmlCData
        private String info;
        @JacksonXmlProperty(isAttribute = true, localName = "msgid")
        private String msgid;
        @JacksonXmlProperty(isAttribute = true, localName = "spnumber")
        private String spnumber;

        public SmsRow() {

        }

        public SmsRow(String phone, String info, String msgid, String spnumber) {
            this.phone = phone;
            this.info = info;
            this.msgid = msgid;
            this.spnumber = spnumber;
        }
    }
}
