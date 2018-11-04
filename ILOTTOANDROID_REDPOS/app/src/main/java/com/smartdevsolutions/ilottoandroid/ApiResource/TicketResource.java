package com.smartdevsolutions.ilottoandroid.ApiResource;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by teecodez on 10/27/2018.
 */

public class TicketResource {
    //id*	integer($int32)
    //agentId	string
    //ticketReferenceId	string
    //masterTicketId	string
    //deviceID	string
    //payoutPin	string
    //walletBalance*	number($decimal)
    //possibleWin*	number($decimal)
    //bonus*	number($decimal)
    //dateRegistered*	string($date-time)
    //amount*	number($decimal)
    //wonAmount*	number($decimal)
    //registrationMethod	IdNameResource{...}
    //game	IdNameResource{...}
    //status	IdNameResource{...}
    //betslips	[...]
    //ispaid*
    private int id;
    private String deviceID;
    private double walletBalance;
    //  private Guid RequestId;
    private double possibleWin;
    private double bonus;
    private Date dateRegistered;
    private double amount;
    private double wonAmount;
    //private String PayoutPin;
    private IdNameResource registrationMethod;
    private IdNameResource game;
    private IdNameResource status;
    private ArrayList<BetSlipResource> betslips;
    private boolean ispaid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public double getPossibleWin() {
        return possibleWin;
    }

    public void setPossibleWin(double possibleWin) {
        this.possibleWin = possibleWin;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getWonAmount() {
        return wonAmount;
    }

    public void setWonAmount(double wonAmount) {
        this.wonAmount = wonAmount;
    }

    public IdNameResource getRegistrationMethod() {
        return registrationMethod;
    }

    public void setRegistrationMethod(IdNameResource registrationMethod) {
        this.registrationMethod = registrationMethod;
    }

    public IdNameResource getGame() {
        return game;
    }

    public void setGame(IdNameResource game) {
        this.game = game;
    }

    public IdNameResource getStatus() {
        return status;
    }

    public void setStatus(IdNameResource status) {
        this.status = status;
    }

    public ArrayList<BetSlipResource> getBetslips() {
        return betslips;
    }

    public void setBetslips(ArrayList<BetSlipResource> betslips) {
        this.betslips = betslips;
    }

    public boolean ispaid() {
        return ispaid;
    }

    public void setIspaid(boolean ispaid) {
        this.ispaid = ispaid;
    }
}
