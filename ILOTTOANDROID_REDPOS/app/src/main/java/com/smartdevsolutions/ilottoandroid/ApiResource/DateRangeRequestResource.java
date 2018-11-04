package com.smartdevsolutions.ilottoandroid.ApiResource;

import java.util.Date;

/**
 * Created by teecodez on 10/27/2018.
 */

public class DateRangeRequestResource {

    private String device;
    private Date startDate;
    private Date endDate;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
