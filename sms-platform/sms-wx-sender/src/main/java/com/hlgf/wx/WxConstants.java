package com.hlgf.wx;

import java.util.HashMap;
import java.util.Map;

public class WxConstants {

    private static String accessToken = null;

    private static Map<String, String> bindInfo = new HashMap<>();

    public static void setBindInfo(Map<String, String> bindInfo) {
        WxConstants.bindInfo = bindInfo;
    }

    public static Map<String, String> getBindInfo() {
        return bindInfo;
    }

    public static void setAccessToken(String accessToken) {
        WxConstants.accessToken = accessToken;
    }

    public static String getAccessToken() {
        return accessToken;
    }
}