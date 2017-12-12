package com.smartdevsolutions.ilottoandroid.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by BINARYCODES on 5/6/2017.
 */

public class DailyGame {
    private int id ;

    private int gameID ;

    private String gametype ;

    private String gameimageUrl ;

    private String gamename ;

    private String gamecode ;

    private String date ;

    private String startdatetime ;

    private String enddatetime ;

    private boolean isValidated ;

    private boolean isActive ;

    private boolean cancombineothers ;
    
    private List<BetType> bettypes ;

    public DailyGame() {
        bettypes = new ArrayList<BetType>();
    }

    public List<BetType> getBettypes() {
        return bettypes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameID() {
        return gameID;
    }

    public Date getDateInstance() {
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date.replace("T"," "));
            return date1;
        }
        catch (Exception ex)
        {
            return  null;
        }
    }

    public Date getStartdatetimeInstance() {
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startdatetime.replace("T"," "));
            return date1;
        }
        catch (Exception ex)
        {
            return  null;
        }
    }

    public Date getenddatetimeInstance() {
        try {

            Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(enddatetime.replace("T"," "));
            return date1;
        }
        catch (Exception ex)
        {
            return  null;
        }
    }


    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getGametype() {
        return gametype;
    }

    public void setGametype(String gametype) {
        this.gametype = gametype;
    }

    public String getGameimageUrl() {
        return gameimageUrl;
    }

    public void setGameimageUrl(String gameimageUrl) {
        this.gameimageUrl = gameimageUrl;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartdatetime() {
        return startdatetime;
    }

    public void setStartdatetime(String startdatetime) {
        this.startdatetime = startdatetime;
    }

    public String getEnddatetime() {
        return enddatetime;
    }

    public void setEnddatetime(String enddatetime) {
        this.enddatetime = enddatetime;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isCancombineothers() {
        return cancombineothers;
    }

    public void setCancombineothers(boolean cancombineothers) {
        this.cancombineothers = cancombineothers;
    }

    public void setBettypes(ArrayList<BetType> bettypes) {
        this.bettypes = bettypes;
    }
}
