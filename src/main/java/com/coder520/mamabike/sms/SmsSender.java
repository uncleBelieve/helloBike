package com.coder520.mamabike.sms;


public interface SmsSender {

     void sendSms(String phone, String tplId, String params);
}
