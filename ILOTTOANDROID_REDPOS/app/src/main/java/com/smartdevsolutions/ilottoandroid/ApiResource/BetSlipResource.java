package com.smartdevsolutions.ilottoandroid.ApiResource;

import java.util.ArrayList;

/**
 * Created by teecodez on 10/27/2018.
 */

public class BetSlipResource {

    private int id;
    private ArrayList<Integer> bet1;
    private ArrayList<Integer> bet2;
    private double stakePerLine;
    private int lines;
    private double possibleBonus;
    private double amount;
    private double wonAmount;
    private IdNameCodeResource betType;
    private IdNameResource status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getBet1() {
        return bet1;
    }

    public void setBet1(ArrayList<Integer> bet1) {
        this.bet1 = bet1;
    }

    public ArrayList<Integer> getBet2() {
        return bet2;
    }

    public void setBet2(ArrayList<Integer> bet2) {
        this.bet2 = bet2;
    }

    public double getStakePerLine() {
        return stakePerLine;
    }

    public void setStakePerLine(double stakePerLine) {
        this.stakePerLine = stakePerLine;
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public double getPossibleBonus() {
        return possibleBonus;
    }

    public void setPossibleBonus(double possibleBonus) {
        this.possibleBonus = possibleBonus;
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

    public IdNameCodeResource getBetType() {
        return betType;
    }

    public void setBetType(IdNameCodeResource betType) {
        this.betType = betType;
    }

    public IdNameResource getStatus() {
        return status;
    }

    public void setStatus(IdNameResource status) {
        this.status = status;
    }

    public String getBet1String() {

        String returnstake = "";
        for (int i = 0; i < bet1.size()-1; i++)
        {
            returnstake += String.format("%02d" , i) + "-";
        }
        return returnstake;
    }

    public String getBet2String() {

        String returnstake = "";
        for (int i = 0; i < bet2.size()-1; i++)
        {
            returnstake += String.format("%02d" , i) + "-";
        }
        return returnstake;
    }


}
