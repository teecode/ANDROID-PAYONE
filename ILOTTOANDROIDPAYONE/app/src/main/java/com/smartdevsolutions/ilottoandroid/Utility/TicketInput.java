package com.smartdevsolutions.ilottoandroid.Utility;

import java.util.ArrayList;

/**
 * Created by BINARYCODES on 5/6/2017.
 */

public class TicketInput {
    private int customerID ;
    private String gamename ;
    private String version ;
    private ArrayList<BetInput> Betslips ;

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getGamename() {
        return gamename;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ArrayList<BetInput> getBetslips() {
        return Betslips;
    }

    public void setBetslips(ArrayList<BetInput> betslips) {
        Betslips = betslips;
    }
}
