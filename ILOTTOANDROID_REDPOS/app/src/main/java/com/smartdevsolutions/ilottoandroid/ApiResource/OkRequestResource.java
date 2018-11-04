package com.smartdevsolutions.ilottoandroid.ApiResource;

/**
 * Created by teecodez on 10/27/2018.
 */

public class OkRequestResource {
    private String Code;
    private String Message;
    private Object data;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
