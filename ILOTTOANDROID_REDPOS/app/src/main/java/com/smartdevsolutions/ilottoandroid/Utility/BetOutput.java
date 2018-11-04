package com.smartdevsolutions.ilottoandroid.Utility;

import java.util.Date;

/**
 * Created by BINARYCODES on 5/6/2017.
 */

public class BetOutput {
    private int id ;
    private double stake_per_line ;
    private int no_of_lines ;
    private double possible_win ;
    private double bonus ;
    private String master_bet_id ;
    private Date date_registered ;
    private String ipAdress ;
    private String stakebet1 ;
    private String stakebet2 ;
    private String nap ;
    private double won_amount ;
    private int Ticket_Registration_Method ;
    private String regMethod ;
    private double amount ;
    private String gamename ;
    private String betname ;
    private String date ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getStake_per_line() {
        return stake_per_line;
    }

    public void setStake_per_line(double stake_per_line) {
        this.stake_per_line = stake_per_line;
    }

    public int getNo_of_lines() {
        return no_of_lines;
    }

    public void setNo_of_lines(int no_of_lines) {
        this.no_of_lines = no_of_lines;
    }

    public double getPossible_win() {
        return possible_win;
    }

    public void setPossible_win(double possible_win) {
        this.possible_win = possible_win;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public String getMaster_bet_id() {
        return master_bet_id;
    }

    public void setMaster_bet_id(String master_bet_id) {
        this.master_bet_id = master_bet_id;
    }

    public Date getDate_registered() {
        return date_registered;
    }

    public void setDate_registered(Date date_registered) {
        this.date_registered = date_registered;
    }

    public String getIpAdress() {
        return ipAdress;
    }

    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }

    public String getStakebet1() {
        return stakebet1;
    }

    public void setStakebet1(String stakebet1) {
        this.stakebet1 = stakebet1;
    }

    public String getStakebet2() {
        return stakebet2;
    }

    public void setStakebet2(String stakebet2) {
        this.stakebet2 = stakebet2;
    }

    public String getNap() {
        return nap;
    }

    public void setNap(String nap) {
        this.nap = nap;
    }

    public double getWon_amount() {
        return won_amount;
    }

    public void setWon_amount(double won_amount) {
        this.won_amount = won_amount;
    }

    public int getTicket_Registration_Method() {
        return Ticket_Registration_Method;
    }

    public void setTicket_Registration_Method(int ticket_Registration_Method) {
        Ticket_Registration_Method = ticket_Registration_Method;
    }

    public String getRegMethod() {
        return regMethod;
    }

    public void setRegMethod(String regMethod) {
        this.regMethod = regMethod;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getGamename() {
        return gamename;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename;
    }

    public String getBetname() {
        return betname;
    }

    public void setBetname(String betname) {
        this.betname = betname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
