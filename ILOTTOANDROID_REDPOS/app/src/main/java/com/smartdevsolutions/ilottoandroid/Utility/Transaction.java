package com.smartdevsolutions.ilottoandroid.Utility;

/**
 * Created by BINARYCODES on 5/6/2017.
 */

public class Transaction {
    private int id ;
    private int custID ;
    private int transactioncategoryid ;
    private String transctioncategoryname ;
    private int transactiontypeid ;
    private double amount ;
    private double balancebefore ;
    private double balanceafter ;
    private String errormessage ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public int getTransactioncategoryid() {
        return transactioncategoryid;
    }

    public void setTransactioncategoryid(int transactioncategoryid) {
        this.transactioncategoryid = transactioncategoryid;
    }

    public String getTransctioncategoryname() {
        return transctioncategoryname;
    }

    public void setTransctioncategoryname(String transctioncategoryname) {
        this.transctioncategoryname = transctioncategoryname;
    }

    public int getTransactiontypeid() {
        return transactiontypeid;
    }

    public void setTransactiontypeid(int transactiontypeid) {
        this.transactiontypeid = transactiontypeid;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalancebefore() {
        return balancebefore;
    }

    public void setBalancebefore(double balancebefore) {
        this.balancebefore = balancebefore;
    }

    public double getBalanceafter() {
        return balanceafter;
    }

    public void setBalanceafter(double balanceafter) {
        this.balanceafter = balanceafter;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }
}
