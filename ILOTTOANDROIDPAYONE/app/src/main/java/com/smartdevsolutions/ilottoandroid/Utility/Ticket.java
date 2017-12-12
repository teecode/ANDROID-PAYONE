package com.smartdevsolutions.ilottoandroid.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by BINARYCODES on 5/6/2017.
 */

public class Ticket {
    private int id ;
    private String dateregistered ;
    private int statusid ;
    private String statusmessage ;
    private String gamename ;
    private String gamecode ;
    private double amount ;
    private double wonamount ;
    private double betcount ;
    private double bonus ;
    private int custID ;
    private String custname ;
    private int regmethod ;
    private int shopID ;
    private boolean ispaid ;
    private ArrayList<Bet> Bets ;
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

    public int getStatusid() {
        return statusid;
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

    public String getGamename() {
        return gamename;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename;
    }

    public String getGamecode() {
        return gamecode;
    }

    public void setGamecode(String gamecode) {
        this.gamecode = gamecode;
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

    public double getBetcount() {
        return betcount;
    }

    public void setBetcount(double betcount) {
        this.betcount = betcount;
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

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
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

    public boolean ispaid() {
        return ispaid;
    }

    public void setIspaid(boolean ispaid) {
        this.ispaid = ispaid;
    }

    public ArrayList<Bet> getBets() {
        return Bets;
    }

    public void setBets(ArrayList<Bet> bets) {
        Bets = bets;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }
}
