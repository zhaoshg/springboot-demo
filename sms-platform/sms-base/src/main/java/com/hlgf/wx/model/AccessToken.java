package com.hlgf.wx.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 微信的AccessToken
 */
@Setter
@Getter
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccessToken {
    private String access_token;
    private Integer expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String errcode;
    private String errmsg;
}
