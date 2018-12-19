package com.hlgf.wx.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WxResponse {
    private String errcode;
    private String errmsg;
    private String msgid;
}
