package com.coder520.mamabike.common.resp;

import com.coder520.mamabike.common.constants.Constants;


//所有方法返回结果
public class ApiResult <T>{

    private int code = Constants.RESP_STATUS_OK;

    private String message;

    private T data;   //返回数据类型不确定

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
