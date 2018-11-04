package com.smartdevsolutions.ilottoandroid.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by BINARYCODES on 5/6/2017.
 */

public class WinningNumber {
    private int dailygameId ;
    private String gamename ;
    private boolean isghana ;
    private String gamedate ;
    private int winning_ball1 ;
    private int winning_ball2 ;
    private int winning_ball3 ;
    private int winning_ball4 ;
    private int winning_ball5 ;
    private int machine_ball1 ;
    private int machine_ball2 ;
    private int machine_ball3 ;
    private int machine_ball4 ;
    private int machine_ball5 ;

    public int getDailygameId() {
        return dailygameId;
    }

    public void setDailygameId(int dailygameId) {
        this.dailygameId = dailygameId;
    }

    public String getGamename() {
        return gamename;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename;
    }

    public boolean isghana() {
        return isghana;
    }

    public void setIsghana(boolean isghana) {
        this.isghana = isghana;
    }

    public String getGamedate() {
        return gamedate;
    }

    public void setGamedate(String gamedate) {
        this.gamedate = gamedate;
    }

    public Date getGameDateInstance() {
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(gamedate.replace("T"," "));
            return date1;
        }
        catch (Exception ex)
        {
            return  null;
        }
    }

    public String getGameShortStringdate() {
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(gamedate);
            return date1.toString();
        }
        catch (Exception ex)
        {
            return  null;
        }
    }

    public int getWinning_ball1() {
        return winning_ball1;
    }

    public void setWinning_ball1(int winning_ball1) {
        this.winning_ball1 = winning_ball1;
    }

    public int getWinning_ball2() {
        return winning_ball2;
    }

    public void setWinning_ball2(int winning_ball2) {
        this.winning_ball2 = winning_ball2;
    }

    public int getWinning_ball3() {
        return winning_ball3;
    }

    public void setWinning_ball3(int winning_ball3) {
        this.winning_ball3 = winning_ball3;
    }

    public int getWinning_ball4() {
        return winning_ball4;
    }

    public void setWinning_ball4(int winning_ball4) {
        this.winning_ball4 = winning_ball4;
    }

    public int getWinning_ball5() {
        return winning_ball5;
    }

    public void setWinning_ball5(int winning_ball5) {
        this.winning_ball5 = winning_ball5;
    }

    public int getMachine_ball1() {
        return machine_ball1;
    }

    public void setMachine_ball1(int machine_ball1) {
        this.machine_ball1 = machine_ball1;
    }

    public int getMachine_ball2() {
        return machine_ball2;
    }

    public void setMachine_ball2(int machine_ball2) {
        this.machine_ball2 = machine_ball2;
    }

    public int getMachine_ball3() {
        return machine_ball3;
    }

    public void setMachine_ball3(int machine_ball3) {
        this.machine_ball3 = machine_ball3;
    }

    public int getMachine_ball4() {
        return machine_ball4;
    }

    public void setMachine_ball4(int machine_ball4) {
        this.machine_ball4 = machine_ball4;
    }

    public int getMachine_ball5() {
        return machine_ball5;
    }

    public void setMachine_ball5(int machine_ball5) {
        this.machine_ball5 = machine_ball5;
    }

    public String getWinningNumbers() {
            return ""+String.format("%02d" , winning_ball1) + "-" + String.format("%02d" , winning_ball2) + "-"+ String.format("%02d" , winning_ball3) + "-"+ String.format("%02d" , winning_ball4) + "-"+ String.format("%02d" , winning_ball5) ;
    }

    public String getMachineNumbers() {
        if(isghana())
            return "";
        return ""+String.format("%02d" , machine_ball1) + "-" + String.format("%02d" , machine_ball2) + "-"+ String.format("%02d" , machine_ball3) + "-"+ String.format("%02d" , machine_ball4) + "-"+ String.format("%02d" , machine_ball5) ;
    }

}
