package com.smartdevsolutions.ilottoandroid.ApiResource;

/**
 * Created by teecodez on 10/27/2018.
 */

public class UpdatePasswordResource {
    private String device;
    private String oldpassword;
    private String newpassword;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }
}
