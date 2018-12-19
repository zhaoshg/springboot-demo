package com.hlgf.wx.message;

import com.hlgf.sms.model.SalesOrder;
import com.hlgf.wx.WxConstants;
import com.hlgf.wx.model.TemplateData;
import com.hlgf.wx.model.TemplateMessage;
import com.hlgf.wx.model.WxResponse;
import com.hlgf.wx.service.WxSenderService;
import com.hlgf.wx.utils.FormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service("salesOrderWxSender")
public class SalesOrderTemplateSender implements TemplateSender<SalesOrder> {


    @Value("${wx.template-id.sales-order}")
    private String templateId;


    @Value("${sms.template.sales-order.item}")
    private String template_item;


    @Value("${wx.content.salesOrder.first}")
    private String first;


    @Autowired
    private WxSenderService wxSenderService;


    @Override
    public String sendWxMsg(SalesOrder entity) {

//        {{first.DATA}}
//        订单编号：{{keyword1.DATA}}
//        支付金额：{{keyword2.DATA}}
//        支付时间：{{keyword3.DATA}}
//        {{remark.DATA}}
        TemplateMessage msg = new TemplateMessage();
        msg.setTemplate_id(templateId);
        msg.setTouser(entity.getOpenId());
        msg.setTopcolor("");
        Map<String, TemplateData> data = new HashMap<>();
        data.put("first", new TemplateData(String.format(first, entity.getGroupName())));
        data.put("keyword1", new TemplateData(entity.getRelatedNum()));
        data.put("keyword2", new TemplateData(FormatUtil.formatMoney(entity.getTotalAmount())));
        data.put("keyword3", new TemplateData(entity.getPassDate()));


        String itemContent = StringUtils.collectionToDelimitedString(entity.getItemList().stream().map(
                i -> String.format(template_item, i.getProductName(), i.getProductSize(),
                        FormatUtil.formatMoney(i.getPrice()), i.getCount(), i.getUnit())).collect(Collectors.toList()
        ), "\n");

        data.put("remark", new TemplateData("商品详情：\n" + itemContent));
        msg.setData(data);
        WxResponse response = wxSenderService.sendTemplateMessage(WxConstants.getAccessToken(), msg);
        return response.getErrcode();
    }
}
