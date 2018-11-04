package com.smartdevsolutions.ilottoandroid.ApiResource;

import java.util.Map;

/**
 * Created by teecodez on 10/27/2018.
 */

public class ActivateDeviceRequestResource {

    private String[] imiEs;
    private String serial;
    private String deviceId;
    private String password;
    private Map<String, String> activateParams;

    public String[] getImiEs() {
        return imiEs;
    }

    public void setImiEs(String[] imiEs) {
        this.imiEs = imiEs;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, String> getActivateParams() {
        return activateParams;
    }

    public void setActivateParams(Map<String, String> activateParams) {
        this.activateParams = activateParams;
    }
}
