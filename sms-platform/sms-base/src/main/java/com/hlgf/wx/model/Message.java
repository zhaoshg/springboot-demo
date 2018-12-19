package com.hlgf.wx.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 用来接收微信的信息推送
 */
@Getter
@Setter
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "xml")
public class Message {
    @JacksonXmlProperty(localName = "URL")
    @JacksonXmlCData
    private String URL;

    @JacksonXmlProperty(localName = "ToUserName")
    @JacksonXmlCData
    private String toUserName;

    @JacksonXmlProperty(localName = "FromUserName")
    @JacksonXmlCData
    private String fromUserName;

    @JacksonXmlProperty(localName = "CreateTime")
    private long createTime;

    //内容类型 text image voice video shortvideo
    @JacksonXmlProperty(localName = "MsgType")
    @JacksonXmlCData
    private String msgType;

    //
    @JacksonXmlProperty(localName = "Content")
    @JacksonXmlCData
    private String content;

    @JacksonXmlProperty(localName = "MsgId")
    private long msgId;

    //图片链接（由系统生成）
    @JacksonXmlProperty(localName = "PicUrl")
    @JacksonXmlCData
    private String picUrl;

    //语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
    @JacksonXmlProperty(localName = "MediaId")
    @JacksonXmlCData
    private String mediaId;

    //视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
    @JacksonXmlProperty(localName = "ThumbMediaId")
    @JacksonXmlCData
    private String thumbMediaId;

    //语音格式，如amr，speex等
    @JacksonXmlProperty(localName = "Format")
    @JacksonXmlCData
    private String format;

    //语音识别结果，UTF8编码
    @JacksonXmlProperty(localName = "Recognition")
    @JacksonXmlCData
    private String recognition;

    //事件为模板消息发送结束
    @JacksonXmlProperty(localName = "Event")
    @JacksonXmlCData
    private String event;

    //事件为模板消息发送结束
    @JacksonXmlProperty(localName = "Status")
    @JacksonXmlCData
    private String status;
}
