package com.smartdevsolutions.ilottoandroid.Utility;

import java.util.Date;

/**
 * Created by BINARYCODES on 5/6/2017.
 */

public class CustomerDateInput {
    private int custID ;
    private int shopID ;
    private Date fromdate ;
    private Date todate ;
    private int status ;
    private int inputmode ;

    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public Date getFromdate() {
        return fromdate;
    }

    public void setFromdate(Date fromdate) {
        this.fromdate = fromdate;
    }

    public Date getTodate() {
        return todate;
    }

    public void setTodate(Date todate) {
        this.todate = todate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getInputmode() {
        return inputmode;
    }

    public void setInputmode(int inputmode) {
        this.inputmode = inputmode;
    }
}
