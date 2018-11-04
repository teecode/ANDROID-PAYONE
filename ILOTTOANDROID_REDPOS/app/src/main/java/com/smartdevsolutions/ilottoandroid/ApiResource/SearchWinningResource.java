package com.smartdevsolutions.ilottoandroid.ApiResource;

import java.util.Date;

/**
 * Created by teecodez on 10/27/2018.
 */

public class SearchWinningResource {
    private Integer gameId;
    private Date startDate;
    private Date endDate;

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
