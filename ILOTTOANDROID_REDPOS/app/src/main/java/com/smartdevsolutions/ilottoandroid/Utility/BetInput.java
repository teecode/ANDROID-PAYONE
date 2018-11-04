package com.smartdevsolutions.ilottoandroid.Utility;

import java.util.List;

/**
 * Created by BINARYCODES on 5/6/2017.
 */

public class BetInput {

    private String nap ;
    private String betname ;
    private String gamename ;
    private String gamecode ;
    private int maxball ;
    private int minball ;
    private double min_stake ;
    private double max_stake ;
    private int min_no_of_balls ;
    private double winfactor ;
    private List<String> bet1 ;
    private List<String> bet2 ;
    private double amount ;
    private double stakeperline ;
    private int lines ;
    private double possiblewinnings ;
    private int gametype ;

    public String getNap() {
        return nap;
    }

    public void setNap(String nap) {
        this.nap = nap;
    }

    public String getBetname() {
        return betname;
    }

    public void setBetname(String betname) {
        this.betname = betname;
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

    public int getMaxball() {
        return maxball;
    }

    public void setMaxball(int maxball) {
        this.maxball = maxball;
    }

    public int getMinball() {
        return minball;
    }

    public void setMinball(int minball) {
        this.minball = minball;
    }

    public double getMin_stake() {
        return min_stake;
    }

    public void setMin_stake(double min_stake) {
        this.min_stake = min_stake;
    }

    public double getMax_stake() {
        return max_stake;
    }

    public void setMax_stake(double max_stake) {
        this.max_stake = max_stake;
    }

    public int getMin_no_of_balls() {
        return min_no_of_balls;
    }

    public void setMin_no_of_balls(int min_no_of_balls) {
        this.min_no_of_balls = min_no_of_balls;
    }

    public double getWinfactor() {
        return winfactor;
    }

    public void setWinfactor(double winfactor) {
        this.winfactor = winfactor;
    }

    public List<String> getBet1() {
        return bet1;
    }

    public void setBet1(List<String> bet1) {
        this.bet1 = bet1;
    }

    public List<String> getBet2() {
        return bet2;
    }

    public void setBet2(List<String> bet2) {
        this.bet2 = bet2;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

    public double getPossiblewinnings() {
        return possiblewinnings;
    }

    public void setPossiblewinnings(double possiblewinnings) {
        this.possiblewinnings = possiblewinnings;
    }

    public int getGametype() {
        return gametype;
    }

    public void setGametype(int gametype) {
        this.gametype = gametype;
    }

    public  String getBet1Text()
    {
        String returnString = "";
        try {
            if (bet1.size() > 0) {
                int loop = 0;
                int size = bet1.size();
                for (String a: bet1)
                {
//                    int x = Integer.parseInt(a);
//                    if (x < 10) {a = "0" + a;}
                    if ((loop + 1) == size)
                    {returnString += a; break;}
                    else
                    {
                        returnString += a + "-";
                    }

                    loop++;
                }
                return returnString;
            } else
                return returnString;
        }
        catch (Exception e)
        {
            return returnString;
        }
    }

    public  String getBet2Text()
    {
        String returnString = "";
        try {
            if (bet2.size() > 0) {
                int loop = 0;
                int size = bet2.size();
                for (String a: bet2)
                {
//                    int x = Integer.parseInt(a);
//                    if (x < 10) {a = "0" + a;}
                    if ((loop + 1) == size)
                    {returnString += a; break;}
                    else
                    {
                         returnString += a + "-";
                    }

                    loop++;
                }
                return returnString;
            } else
                return returnString;
        }
        catch (Exception e)
        {
            return returnString;
        }
    }
}
