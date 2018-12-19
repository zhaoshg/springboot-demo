package com.hlgf.wx.message;

import com.hlgf.sms.model.BalanceEntity;
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

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service("balanceWxSender")
public class BalanceTemplateSender implements TemplateSender<BalanceEntity> {


    @Value("${wx.template-id.balance}")
    private String templateId;

    @Autowired
    private WxSenderService wxSenderService;
    @Value("${wx.content.balance.first}")
    private String first;

    @Value("${wx.content.balance.remark}")
    private String remark;

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String sendWxMsg(BalanceEntity entity) {
//        {{first.DATA}}
//        查询日期：{{keyword1.DATA}}
//        账户余额：{{keyword2.DATA}}
//        {{remark.DATA}}
        TemplateMessage msg = new TemplateMessage();
        msg.setTemplate_id(templateId);
        msg.setTouser(entity.getOpenId());
        msg.setTopcolor("");
        Map<String, TemplateData> data = new HashMap<>();
        data.put("first", new TemplateData(String.format(first, entity.getCustomerName())));
        data.put("keyword1", new TemplateData(formatter.format(LocalDateTime.now())));
        data.put("keyword2", new TemplateData(FormatUtil.formatMoney(entity.getBalance())));
        String content = String.format(remark, StringUtils.isEmpty(entity.getGroupNumber()) ? "（公司联系电话）" : entity.getGroupNumber());
        data.put("remark", new TemplateData(content));
        msg.setData(data);
        WxResponse response = wxSenderService.sendTemplateMessage(WxConstants.getAccessToken(), msg);
        return response.getErrcode();
    }

}
