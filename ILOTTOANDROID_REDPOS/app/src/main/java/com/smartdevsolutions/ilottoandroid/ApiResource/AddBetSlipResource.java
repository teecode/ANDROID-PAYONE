package com.smartdevsolutions.ilottoandroid.ApiResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by teecodez on 10/27/2018.
 */

public class AddBetSlipResource {

    private int game;
    private ArrayList<Integer> bet1;
    private ArrayList<Integer> bet2;
    private int nap;
    private double stakeperline;
    private int lines;

    public int getGame() {
        return game;
    }

    public void setGame(int game) {
        this.game = game;
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

    public int getNap() {
        return nap;
    }

    public void setNap(int nap) {
        this.nap = nap;
    }

    public double getStakeperline() {
        return stakeperline;
    }

    public void setStakeperline(double stakeperline) {
        this.stakeperline = stakeperline;
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public String getBet1String() {

        String returnstake = "";
        for (int i = 0; i < bet1.size()-1; i++)
        {
            returnstake += String.format("%02d" , bet1.get(i)) + "-";
        }
        returnstake +=  String.format("%02d" , bet1.get(bet1.size()-1));
        return returnstake;
    }

    public String getBet2String() {

        String returnstake = "";
        for (int i = 0; i < bet2.size()-1; i++)
        {
            returnstake += String.format("%02d" , bet2.get(i)) + "-";
        }
        returnstake +=  String.format("%02d" , bet2.get(bet2.size()-1));
        return returnstake;
    }
    public double getAmount() {
        return stakeperline *lines;
    }

    public  String getNapName(List<BetTypeResource> betTypes){
        for (BetTypeResource res:betTypes ) {
            if(nap == res.getBetTypeID())
                return res.getNap();
        }
        return  "N/A";
    }
}
