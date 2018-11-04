package com.smartdevsolutions.ilottoandroid.ApiResource;

/**
 * Created by teecodez on 10/27/2018.
 */

public class BetTypeResource {

    private int betTypeID;
    private String nap;

    private String code;

    private double winFactor;

    private int maximumNumberOfBalls;

    private int minimumNumberOfBalls;

    private double minimumStake;

    private double maximumStake;

    private int gameType;

    public int getBetTypeID() {
        return betTypeID;
    }

    public void setBetTypeID(int id) {
        this.betTypeID = id;
    }

    public String getNap() {
        return nap;
    }

    public void setNap(String nap) {
        this.nap = nap;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getWinFactor() {
        return winFactor;
    }

    public void setWinFactor(double winFactor) {
        this.winFactor = winFactor;
    }

    public int getMaximumNumberOfBalls() {
        return maximumNumberOfBalls;
    }

    public void setMaximumNumberOfBalls(int maximumNumberOfBalls) {
        this.maximumNumberOfBalls = maximumNumberOfBalls;
    }

    public int getMinimumNumberOfBalls() {
        return minimumNumberOfBalls;
    }

    public void setMinimumNumberOfBalls(int minimumNumberOfBalls) {
        this.minimumNumberOfBalls = minimumNumberOfBalls;
    }

    public double getMinimumStake() {
        return minimumStake;
    }

    public void setMinimumStake(double minimumStake) {
        this.minimumStake = minimumStake;
    }

    public double getMaximumStake() {
        return maximumStake;
    }

    public void setMaximumStake(double maximumStake) {
        this.maximumStake = maximumStake;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }
}
