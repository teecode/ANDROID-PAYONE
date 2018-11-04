package com.smartdevsolutions.ilottoandroid.ApiResource;

/**
 * Created by teecodez on 10/27/2018.
 */

public class PayoutRequestResource {
    private String device;
    private double amount;
    private String details;
    private String devicePayoutType;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDevicePayoutType() {
        return devicePayoutType;
    }

    public void setDevicePayoutType(String devicePayoutType) {
        this.devicePayoutType = devicePayoutType;
    }
}
