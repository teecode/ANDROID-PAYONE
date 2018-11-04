package com.smartdevsolutions.ilottoandroid.ApiResource;

import java.sql.Time;

/**
 * Created by teecodez on 10/27/2018.
 */

public class GameResource {

    private int id;
    private String name;
    private Time startTime;
    private Time endTime;
    private String day;
    private boolean isGhanaGame;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isGhanaGame() {
        return isGhanaGame;
    }

    public void setGhanaGame(boolean ghanaGame) {
        isGhanaGame = ghanaGame;
    }
}
