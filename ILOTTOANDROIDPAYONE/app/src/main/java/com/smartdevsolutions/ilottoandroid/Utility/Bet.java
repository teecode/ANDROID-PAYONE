package com.smartdevsolutions.ilottoandroid.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by BINARYCODES on 5/6/2017.
 */

public class Bet {
    private int id ;
    private String dateregistered ;
    private int statusid ;
    private String statusmessage ;
    private double amount ;
    private double wonamount ;
    private int nooflines ;
    private double stakeperline ;
    private boolean ispaid ;
    private String bet1 ;
    private String bet2 ;
    private int bettype ;
    private String bettypename ;
    private String bettypenap ;
    private double bonus ;
    private int custID ;
    private int regmethod ;
    private int shopID ;
    private String errormessage ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateregistered() {
        return dateregistered;
    }

    public void setDateregistered(String dateregistered) {
        this.dateregistered = dateregistered;
    }

    public int getStatusid() {
        return statusid;
    }

    public Date getDateInstance() {
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateregistered.replace("T"," "));
            return date1;
        }
        catch (Exception ex)
        {
            return  null;
        }
    }

    public void setStatusid(int statusid) {
        this.statusid = statusid;
    }

    public String getStatusmessage() {
        return statusmessage;
    }

    public void setStatusmessage(String statusmessage) {
        this.statusmessage = statusmessage;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getWonamount() {
        return wonamount;
    }

    public void setWonamount(double wonamount) {
        this.wonamount = wonamount;
    }

    public int getNooflines() {
        return nooflines;
    }

    public void setNooflines(int nooflines) {
        this.nooflines = nooflines;
    }

    public double getStakeperline() {
        return stakeperline;
    }

    public void setStakeperline(double stakeperline) {
        this.stakeperline = stakeperline;
    }

    public boolean ispaid() {
        return ispaid;
    }

    public void setIspaid(boolean ispaid) {
        this.ispaid = ispaid;
    }

    public String getBet1() {
        return bet1;
    }

    public void setBet1(String bet1) {
        this.bet1 = bet1;
    }

    public String getBet1String() {

        String returnstake = "";
        for (int i = 0; i < bet1.length(); i++)
        {
            returnstake += bet1.charAt(i);
            if (i % 2 == 1 && i<(bet1.length()-1))
            {  returnstake += "-"+ bet1.charAt(i) ;}
        }
        return returnstake;
    }

    public String getBet2String() {

        String returnstake = "";
        for (int i = 0; i < bet2.length(); i++)
        {
            returnstake += bet2.charAt(i);
            if (i % 2 == 1 && i<(bet2.length()-1))
                returnstake += "-"+bet2.charAt(i);
        }
        return returnstake;
    }

    public String getBet2() {
        return bet2;
    }

    public void setBet2(String bet2) {
        this.bet2 = bet2;
    }

    public int getBettype() {
        return bettype;
    }

    public void setBettype(int bettype) {
        this.bettype = bettype;
    }

    public String getBettypename() {
        return bettypename;
    }

    public void setBettypename(String bettypename) {
        this.bettypename = bettypename;
    }

    public String getBettypenap() {
        return bettypenap;
    }

    public void setBettypenap(String bettypenap) {
        this.bettypenap = bettypenap;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public int getRegmethod() {
        return regmethod;
    }

    public void setRegmethod(int regmethod) {
        this.regmethod = regmethod;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }
}
