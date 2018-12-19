package com.hlgf.wx.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "xml")
public class WXMessage {
    @JacksonXmlProperty(localName = "URL")
    @JacksonXmlCData(value = true)
    private String URL;

    @JacksonXmlProperty(localName = "ToUserName")
    @JacksonXmlCData(value = true)
    private String toUserName;

    @JacksonXmlProperty(localName = "FromUserName")
    @JacksonXmlCData(value = true)
    private String fromUserName;

    @JacksonXmlProperty(localName = "CreateTime")
    private long createTime;

    //内容类型 text image voice video shortvideo
    @JacksonXmlProperty(localName = "MsgType")
    @JacksonXmlCData(value = true)
    private String msgType;

    //
    @JacksonXmlProperty(localName = "Content")
    @JacksonXmlCData(value = true)
    private String content;

    @JacksonXmlProperty(localName = "MsgId")
    private long msgId;

    //图片链接（由系统生成）
    @JacksonXmlProperty(localName = "PicUrl")
    @JacksonXmlCData(value = true)
    private String picUrl;

    //语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
    @JacksonXmlProperty(localName = "MediaId")
    @JacksonXmlCData(value = true)
    private String mediaId;

    //视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
    @JacksonXmlProperty(localName = "ThumbMediaId")
    @JacksonXmlCData(value = true)
    private String thumbMediaId;

    //语音格式，如amr，speex等
    @JacksonXmlProperty(localName = "Format")
    @JacksonXmlCData(value = true)
    private String format;

    //语音识别结果，UTF8编码
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(localName = "Recognition")
    @JacksonXmlCData(value = true)
    private String recognition;
}
