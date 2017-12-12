package com.smartdevsolutions.ilottoandroid.Utility;

/**
 * Created by BINARYCODES on 5/6/2017.
 */

public class AccountSummaryOutput {
    private int customerID ;
    private String username ;
    private int shopID ;
    private String shopname ;
    private double stake ;
    private double cancelled ;
    private double sales ;
    private double winning ;
    private double commission ;
    private double balance ;
    private double netbalance ;
    private String serviceMessage ;

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public double getStake() {
        return stake;
    }

    public void setStake(double stake) {
        this.stake = stake;
    }

    public double getCancelled() {
        return cancelled;
    }

    public void setCancelled(double cancelled) {
        this.cancelled = cancelled;
    }

    public double getSales() {
        return sales;
    }

    public void setSales(double sales) {
        this.sales = sales;
    }

    public double getWinning() {
        return winning;
    }

    public void setWinning(double winning) {
        this.winning = winning;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getNetbalance() {
        return netbalance;
    }

    public void setNetbalance(double netbalance) {
        this.netbalance = netbalance;
    }

    public String getServiceMessage() {
        return serviceMessage;
    }

    public void setServiceMessage(String serviceMessage) {
        this.serviceMessage = serviceMessage;
    }
}
