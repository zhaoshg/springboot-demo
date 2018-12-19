package com.hlgf.wx.message;

import com.hlgf.sms.model.FundsArrivalsEntity;
import com.hlgf.wx.WxConstants;
import com.hlgf.wx.model.TemplateData;
import com.hlgf.wx.model.TemplateMessage;
import com.hlgf.wx.model.WxResponse;
import com.hlgf.wx.service.WxSenderService;
import com.hlgf.wx.utils.FormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("fundsArrivalsWxSender")
public class FundsArrivalsTemplateSender implements TemplateSender<FundsArrivalsEntity> {


    @Value("${wx.template-id.funds-arrivals}")
    private String templateId;


    @Value("${wx.content.fundsArrivals.first}")
    private String first;

    @Value("${wx.content.fundsArrivals.remark}")
    private String remark;

    @Autowired
    private WxSenderService wxSenderService;

    @Override
    public String sendWxMsg(FundsArrivalsEntity entity) {

//        {{first.DATA}}
//        收款金额：{{keyword1.DATA}}
//        时间：{{keyword2.DATA}}
//        {{remark.DATA}}

        TemplateMessage msg = new TemplateMessage();
        msg.setTemplate_id(templateId);
        msg.setTouser(entity.getOpenId());
        msg.setTopcolor("");
        Map<String, TemplateData> data = new HashMap<>();
        data.put("first", new TemplateData(String.format(first, entity.getGroupName())));
        data.put("keyword1", new TemplateData(FormatUtil.formatMoney(entity.getMoneyCr())));
        data.put("keyword2", new TemplateData(entity.getPassDate()));
        data.put("remark", new TemplateData(String.format(remark, entity.getGroupNumber() == null ? "" : entity.getGroupNumber())));
        msg.setData(data);
        WxResponse response = wxSenderService.sendTemplateMessage(WxConstants.getAccessToken(), msg);

        return response.getErrcode();
    }
}
