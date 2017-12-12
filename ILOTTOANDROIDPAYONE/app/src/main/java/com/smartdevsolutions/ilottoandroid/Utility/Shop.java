package com.smartdevsolutions.ilottoandroid.Utility;

/**
 * Created by BINARYCODES on 5/6/2017.
 */

public class Shop {
    private int id ;
    private String shopname ;
    private String shopcode ;
    private int shoptype ;
    private boolean isactive ;
    private double commission ;
    private int bank ;
    private int state ;
    private String additionaltext ;
    private double walletbalance ;
    private String errormessage ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShopcode() {
        return shopcode;
    }

    public void setShopcode(String shopcode) {
        this.shopcode = shopcode;
    }

    public int getShoptype() {
        return shoptype;
    }

    public void setShoptype(int shoptype) {
        this.shoptype = shoptype;
    }

    public boolean isactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public int getBank() {
        return bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getAdditionaltext() {
        return additionaltext;
    }

    public void setAdditionaltext(String additionaltext) {
        this.additionaltext = additionaltext;
    }

    public double getWalletbalance() {
        return walletbalance;
    }

    public void setWalletbalance(double walletbalance) {
        this.walletbalance = walletbalance;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }
}
