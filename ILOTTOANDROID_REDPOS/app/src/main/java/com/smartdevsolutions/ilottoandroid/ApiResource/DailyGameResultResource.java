package com.smartdevsolutions.ilottoandroid.ApiResource;

/**
 * Created by teecodez on 10/27/2018.
 */

public class DailyGameResultResource
{

    private int winningBall1;
    private int winningBall2;
    private int winningBall3;
    private int winningBall4;
    private int winningBall5;
    private int machineBall1;
    private int machineBall2;
    private int machineBall3;
    private int machineBall4;
    private int machineBall5;

    public int getWinningBall1() {
        return winningBall1;
    }

    public void setWinningBall1(int winningBall1) {
        this.winningBall1 = winningBall1;
    }

    public int getWinningBall2() {
        return winningBall2;
    }

    public void setWinningBall2(int winningBall2) {
        this.winningBall2 = winningBall2;
    }

    public int getWinningBall3() {
        return winningBall3;
    }

    public void setWinningBall3(int winningBall3) {
        this.winningBall3 = winningBall3;
    }

    public int getWinningBall4() {
        return winningBall4;
    }

    public void setWinningBall4(int winningBall4) {
        this.winningBall4 = winningBall4;
    }

    public int getWinningBall5() {
        return winningBall5;
    }

    public void setWinningBall5(int winningBall5) {
        this.winningBall5 = winningBall5;
    }

    public int getMachineBall1() {
        return machineBall1;
    }

    public void setMachineBall1(int machineBall1) {
        this.machineBall1 = machineBall1;
    }

    public int getMachineBall2() {
        return machineBall2;
    }

    public void setMachineBall2(int machineBall2) {
        this.machineBall2 = machineBall2;
    }

    public int getMachineBall3() {
        return machineBall3;
    }

    public void setMachineBall3(int machineBall3) {
        this.machineBall3 = machineBall3;
    }

    public int getMachineBall4() {
        return machineBall4;
    }

    public void setMachineBall4(int machineBall4) {
        this.machineBall4 = machineBall4;
    }

    public int getMachineBall5() {
        return machineBall5;
    }

    public void setMachineBall5(int machineBall5) {
        this.machineBall5 = machineBall5;
    }

    public  String getWInningBallString(){
        return winningBall1 + " " +winningBall2+ " "+ winningBall3 + " "+ winningBall4 + " "+ winningBall5 ;
    }
    public  String getMachineBallString(){
        return machineBall1 + " " +machineBall2+ " "+ machineBall3 + " "+ machineBall4 + " "+ machineBall5 ;
    }
}
