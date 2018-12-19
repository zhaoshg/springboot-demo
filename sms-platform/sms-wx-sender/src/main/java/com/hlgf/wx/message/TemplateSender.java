package com.hlgf.wx.message;

public interface TemplateSender<T> {
    String sendWxMsg(T t);
}