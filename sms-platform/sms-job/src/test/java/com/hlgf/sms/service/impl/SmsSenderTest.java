package com.hlgf.sms.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.hlgf.sms.Constants;
import com.hlgf.sms.TestTemplate;
import com.hlgf.sms.model.SmsSendLog;
import com.hlgf.sms.service.MsgSender;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class SmsSenderTest extends TestTemplate {


    private MsgSender msgSender;

    @Test
    public void testSender() {
        Map<String, SmsSendLog> map = new HashMap<>();
        SmsSendLog sms = new SmsSendLog();
        sms.setContent("【广西辉隆农业服务有限公司】收款提醒：本公司于2018年06月30日13时15分收到您支付的货款￥2,000.00元，本短信不做为收款凭据。如有异议请于3日内回电0551-62667301（公司联系电话）。");
        sms.setSmsType(Constants.SmsType.SMS_TYPE_FUNDS_ARRIVALS_NOTIFICATION);
        sms.setSendState("Y");
        sms.setRelatedId("1001A1100000001GP6MU");
        sms.setPkGroup("0001T110000000001GXL");
        sms.setGroupName("广西辉隆农业服务有限公司");
        sms.setSendCnt(1);
        sms.setSendTime("2018-11-22 08:49:00");
        sms.setWordCount(56);
        sms.setPkReceiver("");
        sms.setReceiverName("柳州市李承华450222196802010615");
        sms.setReceiverNumber("18556515624");
        map.put(sms.getRelatedId(), sms);
        msgSender.betchSend(map);
    }


    @Test
    public void testObject2XML() {
        String xmlStr = "<?xml version='1.0' encoding='utf-8'?><string xmlns='http://access.xx95.net:8886/'>01</string>";
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JaxbAnnotationModule());
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        xmlMapper.setDefaultUseWrapper(false);
        //字段为null，自动忽略，不再序列化
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //XML标签名:使用骆驼命名的属性名，
//        xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        //设置转换模式
        xmlMapper.enable(MapperFeature.USE_STD_BEAN_NAMING);

        try {
            MsgSenderImpl.SmsContent content = new MsgSenderImpl.SmsContent();
            List<MsgSenderImpl.SmsRow> rows = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                MsgSenderImpl.SmsRow row = new MsgSenderImpl.SmsRow(String.valueOf(i), String.valueOf(i), String.valueOf(i), String.valueOf(i));
                rows.add(row);
            }
            content.setRow(rows);
            String result = xmlMapper.writeValueAsString(content);
            System.out.println("序列化结果：" + result);
            MsgSenderImpl.SmsContent contents2 = xmlMapper.readValue(result, MsgSenderImpl.SmsContent.class);   //readValue(xmlStr, new TypeReference<CUCCSenderImpl.SmsContent>() {});
            System.out.println(contents2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
