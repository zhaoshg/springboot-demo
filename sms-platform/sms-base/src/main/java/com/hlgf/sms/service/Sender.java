package com.hlgf.sms.service;

public interface Sender {
    boolean smsSend(String content, String number, String msgId);
}
