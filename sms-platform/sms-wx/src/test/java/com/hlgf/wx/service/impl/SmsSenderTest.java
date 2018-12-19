package com.hlgf.wx.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hlgf.wx.TestTemplate;
import com.hlgf.wx.model.TemplateData;
import com.hlgf.wx.model.TemplateMessage;
import com.hlgf.wx.model.WxResponse;
import com.hlgf.wx.service.WxService;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Log4j2
@Component
public class SmsSenderTest extends TestTemplate {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private WxService wxService;

    @Value("${wx.template-id.work-list}")
    private String workListTemplate;

    @Value("${wx.sendTemplateMessage}")
    private String sendTemplateMsgUrl;


//    @Test
//    public void testSendWxMsg() throws JsonProcessingException {
//        TemplateMessage msg = new TemplateMessage();
//        msg.setTemplate_id(workListTemplate);
//        msg.setTouser("o0Vbp5ovUEguP6KlMNisHm4PDNPQ");
//
//        Map<String, TemplateData> data = new HashMap<>();
//
//        TemplateData sysname = new TemplateData();
//        sysname.setValue("【NC】");
//        data.put("sysname", sysname);
//
//        TemplateData sendTime = new TemplateData();
//        sendTime.setValue(LocalDateTime.now().format(formatter));
//        data.put("sendTime", sendTime);
//
//        TemplateData content = new TemplateData();
//        content.setValue("这里放的是提醒事项。");
//        data.put("content", content);
//
//        msg.setData(data);
//        ObjectMapper objectMapper = new ObjectMapper();
//        System.out.println(objectMapper.writeValueAsString(msg));
//
////        WxResponse response = wxService.sendTemplateMessage("16_617Kd3H9bkEz2oXJxHCbCQRAntX_8-sLxUL2tyHKnB_ASqWjEp-W7dMOVPhVvJwD0bOz1bDoqSpTl_hGlExeUOg3c_QvYnCdOanBSUx-QCxd4xnwCY60_BIvcc1hrkkk5NQ7ZgK60sHzvFCZPIGbAGAEXV", msg);
//        WxResponse response = wxService.sendTemplateMessage("16_1v0WORWwk5jCZIn_PUrhUFcIWlBgDg5xzTYYUuSa5-mCUraKwc1Hj4xZpVHCU8EzNhFvvpFU4VbYdEp_z4KmRd0dBBjz_lIXhSWaPrgObqvp9qlsh5excyoRwnxQXVn1PC54PYBPtyMeRDaFYGVdABACKZ", msg);
////        Assert.assertEquals("0",response.getErrcode());
//        Assert.assertEquals("42001", response.getErrcode());
//
//    }


    @Test
    public void testStr() {

        String sources = "0123456789"; // 加上一些字母，就可以生成pc站的验证码了
        Random rand = new Random();
        StringBuffer flag = new StringBuffer();
        for (int j = 0; j < 6; j++) {
            flag.append(sources.charAt(rand.nextInt(9)) + "");
        }

    }


//    @Resource(name = "CUCCSender")
//    private SMSSender msgSender;
//
//    @Test
//    public void testSender() {
//        Map<String, SmsSendLog> map = new HashMap<>();
//        SmsSendLog sms = new SmsSendLog();
//        sms.setContent("【广西辉隆农业服务有限公司】收款提醒：本公司于2018年06月30日13时15分收到您支付的货款￥2,000.00元，本短信不做为收款凭据。如有异议请于3日内回电0551-62667301（公司联系电话）。");
//        sms.setSmsType(Constants.SmsType.SMS_TYPE_FUNDS_ARRIVALS_NOTIFICATION);
//        sms.setSendState("Y");
//        sms.setRelatedId("1001A1100000001GP6MU");
//        sms.setPkGroup("0001T110000000001GXL");
//        sms.setGroupName("广西辉隆农业服务有限公司");
//        sms.setSendCnt(1);
//        sms.setSendTime("2018-11-22 08:49:00");
//        sms.setWordCount(56);
//        sms.setPkReceiver("");
//        sms.setReceiverName("柳州市李承华450222196802010615");
//        sms.setReceiverNumber("18556515624");
//        map.put(sms.getRelatedId(),sms);
//        msgSender.betchSend(map);
//    }
//
//
//    @Test
//    public void testObject2XML() {
//        String xmlStr = "<?xml version='1.0' encoding='utf-8'?><string xmlns='http://access.xx95.net:8886/'>01</string>";
//        XmlMapper xmlMapper = new XmlMapper();
//        xmlMapper.registerModule(new JaxbAnnotationModule());
//        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        xmlMapper.setDefaultUseWrapper(false);
//        //字段为null，自动忽略，不再序列化
//        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        //XML标签名:使用骆驼命名的属性名，
////        xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
//        //设置转换模式
//        xmlMapper.enable(MapperFeature.USE_STD_BEAN_NAMING);
//
//        try {
//            CUCCSenderImpl.SmsContent content = new CUCCSenderImpl.SmsContent();
//            List<CUCCSenderImpl.SmsRow> rows = new ArrayList<>();
//            for (int i = 0; i < 5; i++) {
//                CUCCSenderImpl.SmsRow row = new CUCCSenderImpl.SmsRow(String.valueOf(i), String.valueOf(i), String.valueOf(i), String.valueOf(i));
//                rows.add(row);
//            }
//            content.setRow(rows);
//            String result = xmlMapper.writeValueAsString(content);
//            System.out.println("序列化结果：" + result);
//            CUCCSenderImpl.SmsContent contents2 = xmlMapper.readValue(result, CUCCSenderImpl.SmsContent.class);   //readValue(xmlStr, new TypeReference<CUCCSenderImpl.SmsContent>() {});
//            System.out.println(contents2);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
