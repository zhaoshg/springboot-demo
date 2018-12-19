package com.hlgf.wx.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Data
public class  TemplateMessage {
    private String touser;
    private String topcolor="#000000";
    //模板消息ID
    private String template_id;
    //URL置空，在发送后，点模板消息进入一个空白页面（ios），或无法点击（android）。
    private String url;
    //模板详细信息
    private Map<String, TemplateData> data;
}