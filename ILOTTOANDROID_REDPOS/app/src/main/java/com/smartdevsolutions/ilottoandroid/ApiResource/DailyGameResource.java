package com.smartdevsolutions.ilottoandroid.ApiResource;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by teecodez on 10/27/2018.
 */

public class DailyGameResource {

    private String gameName;
    private int gameID;
    private String gameType;
    private String gameImageUrl;
    private String gameCode;
    private Date date;
    private Date startDateTime;
    private Date endDateTime;
    private boolean isValidated;
    private boolean isActive;
    private boolean isGhanaGame;
    private ArrayList<BetTypeResource> betTypes;
    private DailyGameResultResource result;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getGameImageUrl() {
        return gameImageUrl;
    }

    public void setGameImageUrl(String gameImageUrl) {
        this.gameImageUrl = gameImageUrl;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public java.util.Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(java.util.Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public java.util.Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(java.util.Date endDateTime) {
        this.endDateTime = endDateTime;
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

    public boolean isGhanaGame() {
        return isGhanaGame;
    }

    public void setGhanaGame(boolean ghanaGame) {
        isGhanaGame = ghanaGame;
    }

    public ArrayList<BetTypeResource> getBetTypes() {
        return betTypes;
    }

    public void setBetTypes(ArrayList<BetTypeResource> betTypes) {
        this.betTypes = betTypes;
    }

    public DailyGameResultResource getResult() {
        return result;
    }

    public void setResult(DailyGameResultResource result) {
        this.result = result;
    }
}
