package com.smartdevsolutions.ilottoandroid.Utility;

import java.util.Date;

/**
 * Created by BINARYCODES on 5/6/2017.
 */

public class AccountSummaryInput {
    private int customer_id ;
    private int shop_id ;
    private Date fromdate ;
    private Date todate ;
    private int inputmode ;

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
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

    public int getInputmode() {
        return inputmode;
    }

    public void setInputmode(int inputmode) {
        this.inputmode = inputmode;
    }
}
