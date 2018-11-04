package com.smartdevsolutions.ilottoandroid.ApiResource;

import java.util.ArrayList;

/**
 * Created by teecodez on 10/27/2018.
 */

public class RegisterDeviceRequestResource {

    private ArrayList<String> imiEs;
    private String serial;

    public ArrayList<String> getImiEs() {
        return imiEs;
    }

    public void setImiEs(ArrayList<String> imiEs) {
        this.imiEs = imiEs;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
}
