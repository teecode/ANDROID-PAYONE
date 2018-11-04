package com.smartdevsolutions.ilottoandroid.Utility;

/**
 * Created by BINARYCODES on 5/6/2017.
 */

public class BetType {
    private int id ;

    private String NAP ;

    private String code ;

    private double winfactor ;

    private int maxnoofballs ;

    private int minnoofballs ;

    private double minstake ;

    private double maxstake ;

    private int gametype ;

    private int order ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNAP() {
        return NAP;
    }

    public void setNAP(String NAP) {
        this.NAP = NAP;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getWinfactor() {
        return winfactor;
    }

    public void setWinfactor(double winfactor) {
        this.winfactor = winfactor;
    }

    public int getMaxnoofballs() {
        return maxnoofballs;
    }

    public void setMaxnoofballs(int maxnoofballs) {
        this.maxnoofballs = maxnoofballs;
    }

    public int getMinnoofballs() {
        return minnoofballs;
    }

    public void setMinnoofballs(int minnoofballs) {
        this.minnoofballs = minnoofballs;
    }

    public double getMinstake() {
        return minstake;
    }

    public void setMinstake(double minstake) {
        this.minstake = minstake;
    }

    public double getMaxstake() {
        return maxstake;
    }

    public void setMaxstake(double maxstake) {
        this.maxstake = maxstake;
    }

    public int getGametype() {
        return gametype;
    }

    public void setGametype(int gametype) {
        this.gametype = gametype;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
