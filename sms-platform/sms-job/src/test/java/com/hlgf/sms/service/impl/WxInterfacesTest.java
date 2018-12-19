package com.hlgf.sms.service.impl;

import com.hlgf.sms.TestTemplate;
import com.hlgf.wx.WxConstants;
import com.hlgf.wx.model.TemplateData;
import com.hlgf.wx.model.TemplateMessage;
import com.hlgf.wx.model.WxResponse;
import com.hlgf.wx.service.WxSenderService;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 微信相关接口测试
 */
@Log4j2
public class WxInterfacesTest extends TestTemplate {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WxSenderService wxSenderService;

//    /**
//     * 测试微信获取素材接口
//     *
//     * @throws UnsupportedEncodingException
//     */
//    @Test
//    public void testBatchGetMaterial() throws UnsupportedEncodingException {
//        String url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=%s";
//        String accessToken = "16_2uYhAxaAhiIDVG2C0FySBrB7dYTtnmbreUowJDtQGS0sflpUrBxt3MbAl4ZJ_aPQI8-8ih6AHyZrwqBfKDLUEEsIQ2pcsdwS59EyK9OISxC-E3oarao_rVtmk4HLN5005sLqbfLZ8DDFL-DcKGRaAIAWSD";
//        String formatUrl = String.format(url, accessToken);
//        String param = "{\"type\":\"news\",\"offset\":0,\"count\":20}";
//        String response = restTemplate.postForObject(formatUrl, param, String.class);
//        String json = new String(response.getBytes("ISO-8859-1"), "UTF-8");
//        log.info(json);
//    }

    private ArrayList<String> openIdList = new ArrayList<>(Arrays.asList("o0Vbp5ovUEguP6KlMNisHm4PDNPQ", "o0Vbp5gB8JZO21rTTdDVZkzeHd6w", "o0Vbp5l90CV7qJ3VQ-_7SO7GKnG0", "o0Vbp5ncEOFkvkMF5JZphhkjQHJo"));

    @Test
    public void testFundsArrivals() {
        TemplateMessage msg = new TemplateMessage();
        msg.setTemplate_id("9iSg5zJ8j0OcvzTFOSZ2rmXS3ckTxlKaGypLInLFScw");
        Map<String, TemplateData> data = new HashMap<>();
        data.put("first", new TemplateData("【广西辉隆农业服务有限公司】提醒：您的款项已收到"));
        data.put("keyword1", new TemplateData("￥460.00"));
        data.put("keyword2", new TemplateData("2018年12月11日11时05分"));
        data.put("remark", new TemplateData("本短信不做为收款凭据。如有异议请于3日内回电0551-62667301（公司联系电话）。"));
        msg.setData(data);
        for (String openId : openIdList) {
            msg.setTouser(openId);
            wxSenderService.sendTemplateMessage("16_HDWtIefX7jXSJwLQTQZFk9o-OD0exlqbfpPYh-XnCggkOlOwJa8-ruJ3v7I15h8qw3pnU2v1Dz405F4a9sVwOvyngZDcnxzWF2BY3Q6LA_-00Db7U25-6b5QJEA9CcJMqa7gWtjBeLDJtoZDBTHcACALDR", msg);
        }

    }


    @Test
    public void testSaleOrder() {
        TemplateMessage msg = new TemplateMessage();
        msg.setTemplate_id("DJVmb4-r0sFwAmG5FLe1cs_troPhW3q3BpA2nNDHUkU");
        msg.setTopcolor("");
        Map<String, TemplateData> data = new HashMap<>();
        data.put("first", new TemplateData("【广西辉隆农业服务有限公司】提醒：您的采购订单已收到"));
        data.put("keyword1", new TemplateData("SO302018112800285578"));
        data.put("keyword2", new TemplateData("￥17,510.00"));
        data.put("keyword3", new TemplateData("2018年12月11日11时05分"));
        String itemContent = "山东东平湖中颗粒尿素(50Kg/袋)，单价￥2,060.00元，数量8.5吨";
        data.put("remark", new TemplateData("商品详情：\n" + itemContent));
        msg.setData(data);
        for (String openId : openIdList) {
            msg.setTouser(openId);
            wxSenderService.sendTemplateMessage("16_HDWtIefX7jXSJwLQTQZFk9o-OD0exlqbfpPYh-XnCggkOlOwJa8-ruJ3v7I15h8qw3pnU2v1Dz405F4a9sVwOvyngZDcnxzWF2BY3Q6LA_-00Db7U25-6b5QJEA9CcJMqa7gWtjBeLDJtoZDBTHcACALDR", msg);
        }
    }

    @Test
    public void testAlert() {
//        {{first.DATA}}
//        事件名称：{{keyword1.DATA}}
//        发起人：{{keyword2.DATA}}
//        时间：{{keyword3.DATA}}
//        {{remark.DATA}}
        TemplateMessage msg = new TemplateMessage();
        msg.setTemplate_id("XwfyxSBATjLPJWPQek2HCouhXF4OyEP1w02svlB-xC0");
        msg.setTopcolor("");
        Map<String, TemplateData> data = new HashMap<>();
        data.put("first", new TemplateData("你好，你有新的待办事项"));
        data.put("keyword1", new TemplateData("叶文斌 提交的销售订单待审批！"));
        data.put("keyword2", new TemplateData("【NC】"));
        data.put("keyword3", new TemplateData("2018年11月28日16时41分"));
        data.put("remark", new TemplateData("请尽快处理。"));
        msg.setData(data);
        for (String openId : openIdList) {
            msg.setTouser(openId);
            wxSenderService.sendTemplateMessage("16_HDWtIefX7jXSJwLQTQZFk9o-OD0exlqbfpPYh-XnCggkOlOwJa8-ruJ3v7I15h8qw3pnU2v1Dz405F4a9sVwOvyngZDcnxzWF2BY3Q6LA_-00Db7U25-6b5QJEA9CcJMqa7gWtjBeLDJtoZDBTHcACALDR", msg);
        }
    }

    @Test
    public void testBalance() {
        //        {{first.DATA}}
//        事件名称：{{keyword1.DATA}}
//        发起人：{{keyword2.DATA}}
//        时间：{{keyword3.DATA}}
//        {{remark.DATA}}
        TemplateMessage msg = new TemplateMessage();
        msg.setTemplate_id("XwfyxSBATjLPJWPQek2HCouhXF4OyEP1w02svlB-xC0");
        msg.setTopcolor("");
        Map<String, TemplateData> data = new HashMap<>();
        data.put("first", new TemplateData("余额提醒"));
        data.put("keyword1", new TemplateData("余额提醒"));
        data.put("keyword2", new TemplateData("【安徽瑞美福植物保护有限公司】"));
        data.put("keyword3", new TemplateData("2018年11月28日16时41分"));
        data.put("remark", new TemplateData("寿县保义镇谢岗村王康富加盟店(王康富)客户您好，截止2018年12月11日11时29分，您的账户余额是￥2,200.00元，切勿向任何人泄漏，如有异议请于3日内致电（公司联系电话）。"));
        msg.setData(data);
        for (String openId : openIdList) {
            msg.setTouser(openId);
            wxSenderService.sendTemplateMessage("16_HDWtIefX7jXSJwLQTQZFk9o-OD0exlqbfpPYh-XnCggkOlOwJa8-ruJ3v7I15h8qw3pnU2v1Dz405F4a9sVwOvyngZDcnxzWF2BY3Q6LA_-00Db7U25-6b5QJEA9CcJMqa7gWtjBeLDJtoZDBTHcACALDR", msg);
        }
    }
}
