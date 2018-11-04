package com.smartdevsolutions.ilottoandroid.ApiResource;

import java.util.Date;

/**
 * Created by teecodez on 10/27/2018.
 */

public class TicketPayoutResource {
    //device	string
    //ticket	string
    //payoutPin	string
    //amount*	number($decimal)
    //isPaid	boolean
    //details	string
    //payoutDate*	string($date-time)
    //referenceId
    private String device;
    private String ticket;
    private String payoutPin;
    private IdNameResource devicePayoutType;
    private IdNameResource devicePayoutStatus;
    private double amount;
    private Boolean isPaid;
    private Integer shop;
    private String details;
    private Date payoutDate;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getPayoutPin() {
        return payoutPin;
    }

    public void setPayoutPin(String payoutPin) {
        this.payoutPin = payoutPin;
    }

    public IdNameResource getDevicePayoutType() {
        return devicePayoutType;
    }

    public void setDevicePayoutType(IdNameResource devicePayoutType) {
        this.devicePayoutType = devicePayoutType;
    }

    public IdNameResource getDevicePayoutStatus() {
        return devicePayoutStatus;
    }

    public void setDevicePayoutStatus(IdNameResource devicePayoutStatus) {
        this.devicePayoutStatus = devicePayoutStatus;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public Integer getShop() {
        return shop;
    }

    public void setShop(Integer shop) {
        this.shop = shop;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getPayoutDate() {
        return payoutDate;
    }

    public void setPayoutDate(Date payoutDate) {
        this.payoutDate = payoutDate;
    }
}
