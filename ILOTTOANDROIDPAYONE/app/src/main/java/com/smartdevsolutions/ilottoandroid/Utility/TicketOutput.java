package com.smartdevsolutions.ilottoandroid.Utility;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by BINARYCODES on 5/6/2017.
 */

public class TicketOutput {
    private int id ;
    private double possible_win ;
    private double bonus ;
    private Date date_registered ;
    private double won_amount ;
    private int Ticket_Registration_Method ;
    private String regMethod ;
    private double amount ;
    private String gamename ;
    private String date ;
    private String ret ;
    private String CreationMessage ;
    private ArrayList<BetOutput> Bets ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getDate_registered() {
        return date_registered;
    }

    public void setDate_registered(Date date_registered) {
        this.date_registered = date_registered;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getCreationMessage() {
        return CreationMessage;
    }

    public void setCreationMessage(String creationMessage) {
        CreationMessage = creationMessage;
    }

    public ArrayList<BetOutput> getBets() {
        return Bets;
    }

    public void setBets(ArrayList<BetOutput> bets) {
        Bets = bets;
    }
}
