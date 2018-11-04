package com.smartdevsolutions.ilottoandroid.ApiResource;

import java.util.Date;
import java.util.Map;

/**
 * Created by teecodez on 10/27/2018.
 */

public class DeviceResource {

    private String token;
    private String serial;
    private String password;
    private Date reActivateDate;
    private Map<String, String> activateParams;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getReActivateDate() {
        return reActivateDate;
    }

    public void setReActivateDate(Date reActivateDate) {
        this.reActivateDate = reActivateDate;
    }

    public Map<String, String> getActivateParams() {
        return activateParams;
    }

    public void setActivateParams(Map<String, String> activateParams) {
        this.activateParams = activateParams;
    }
}
