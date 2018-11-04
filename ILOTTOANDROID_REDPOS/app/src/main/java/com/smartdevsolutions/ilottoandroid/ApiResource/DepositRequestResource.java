package com.smartdevsolutions.ilottoandroid.ApiResource;

import java.util.Date;

/**
 * Created by teecodez on 10/27/2018.
 */

public class DepositRequestResource {
    private int Id;
    private String Device;
    private Double Amount;
    private String Details;
    private String RemoteData;
    private Date DepositDate;
    private String TransactionRefrence;
    private Integer ServiceProvider = null;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDevice() {
        return Device;
    }

    public void setDevice(String device) {
        Device = device;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getRemoteData() {
        return RemoteData;
    }

    public void setRemoteData(String remoteData) {
        RemoteData = remoteData;
    }

    public Date getDepositDate() {
        return DepositDate;
    }

    public void setDepositDate(Date depositDate) {
        DepositDate = depositDate;
    }

    public String getTransactionRefrence() {
        return TransactionRefrence;
    }

    public void setTransactionRefrence(String transactionRefrence) {
        TransactionRefrence = transactionRefrence;
    }

    public Integer getServiceProvider() {
        return ServiceProvider;
    }

    public void setServiceProvider(Integer serviceProvider) {
        ServiceProvider = serviceProvider;
    }
}
