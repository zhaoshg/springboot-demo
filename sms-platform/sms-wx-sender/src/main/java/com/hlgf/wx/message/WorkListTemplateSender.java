package com.hlgf.wx.message;

import com.hlgf.sms.model.WorkListEntity;
import com.hlgf.wx.WxConstants;
import com.hlgf.wx.model.TemplateData;
import com.hlgf.wx.model.TemplateMessage;
import com.hlgf.wx.model.WxResponse;
import com.hlgf.wx.service.WxSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Service("worklistWxSender")
public class WorkListTemplateSender implements TemplateSender<WorkListEntity> {

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Value("${wx.template-id.work-list}")
    private String templateId;
    @Autowired
    private WxSenderService wxSenderService;
    private String title = "你好，你有新的待办事项";

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String sendWxMsg(WorkListEntity entity) {
//        {{first.DATA}}
//        事件名称：{{keyword1.DATA}}
//        发起人：{{keyword2.DATA}}
//        时间：{{keyword3.DATA}}
//        {{remark.DATA}}
        TemplateMessage msg = new TemplateMessage();
        msg.setTemplate_id(templateId);
        msg.setTouser(entity.getOpenId());
        msg.setTopcolor("");
        Map<String, TemplateData> data = new HashMap<>();
        data.put("first", new TemplateData(title));
        data.put("keyword1", new TemplateData(entity.getSubject()));
        data.put("keyword2", new TemplateData("【NC】"));
        data.put("keyword3", new TemplateData(formatter.format(entity.getSendTime())));
        data.put("remark", new TemplateData("请尽快处理。"));
        msg.setData(data);
        WxResponse response = wxSenderService.sendTemplateMessage(WxConstants.getAccessToken(), msg);
        return response.getErrcode();
    }
}
