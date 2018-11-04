package com.smartdevsolutions.ilottoandroid.ApiResource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by teecodez on 10/27/2018.
 */

public class AddTicketResource {

    private String deviceID;
    private int game;
    private String version;
    private String requestId;
    private String latitude;
    private String longtitude;
    private ArrayList<AddBetSlipResource> betslips;


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }




    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public int getGame() {
        return game;
    }

    public void setGame(int game) {
        this.game = game;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId() {
        requestId = UUID.randomUUID().toString().replace("-", "");
    }

    public ArrayList<AddBetSlipResource> getBetslips() {
        return betslips;
    }

    public void setBetslips(ArrayList<AddBetSlipResource> betslips) {
        this.betslips = betslips;
    }

    public String getGamename(List<DailyGameResource> fetchedGames) {
        for (DailyGameResource dgr:fetchedGames
             ) {
            if(dgr.getGameID() == game)
            return  dgr.getGameName();
        }
        return  "N/A";
    }
}
