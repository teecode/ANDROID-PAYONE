package com.smartdevsolutions.ilottoandroid.Utility;

/**
 * Created by BINARYCODES on 5/6/2017.
 */

public class Customer {
    private int id ;
    private String firstname ;
    private String lastname ;
    private String middlename ;
    private String username ;
    private double walletbalance ;
    private int isactive ;
    private String mobile ;
    private String email ;
    private int usertype ;
    private int operationmode ;
    private int customercategory ;
    private int customertype ;
    private int shopid ;
    private String shopname ;
    private String shopcode ;
    private double shopwalletbalance ;
    private String acctName ;
    private String acctNumb ;
    private String Bank ;
    private double shopcommison ;
    private double principalagentcommmision ;
    private double subagentcommmision ;
    private String errormessage ;
    private String AuthHash;

    public int getIsactive() {
        return isactive;
    }

    public double getPrincipalagentcommmision() {
        return principalagentcommmision;
    }

    public double getShopcommison() {
        return shopcommison;
    }

    public double getShopwalletbalance() {
        return shopwalletbalance;
    }

    public double getSubagentcommmision() {
        return subagentcommmision;
    }

    public double getWalletbalance() {
        return walletbalance;
    }

    public int getCustomercategory() {
        return customercategory;
    }

    public int getCustomertype() {
        return customertype;
    }

    public int getId() {
        return id;
    }

    public int getOperationmode() {
        return operationmode;
    }

    public int getShopid() {
        return shopid;
    }

    public int getUsertype() {
        return usertype;
    }

    public String getAcctName() {
        return acctName;
    }

    public String getAcctNumb() {
        return acctNumb;
    }

    public String getAuthHash() {
        return AuthHash;
    }

    public String getBank() {
        return Bank;
    }

    public String getEmail() {
        return email;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public String getMobile() {
        return mobile;
    }

    public String getShopcode() {
        return shopcode;
    }

    public String getShopname() {
        return shopname;
    }

    public String getUsername() {
        return username;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public void setAcctNumb(String acctNumb) {
        this.acctNumb = acctNumb;
    }

    public void setAuthHash(String authHash) {
        AuthHash = authHash;
    }

    public void setBank(String bank) {
        Bank = bank;
    }

    public void setCustomercategory(int customercategory) {
        this.customercategory = customercategory;
    }

    public void setCustomertype(int customertype) {
        this.customertype = customertype;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIsactive(int isactive) {
        this.isactive = isactive;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setOperationmode(int operationmode) {
        this.operationmode = operationmode;
    }

    public void setPrincipalagentcommmision(double principalagentcommmision) {
        this.principalagentcommmision = principalagentcommmision;
    }

    public void setShopcode(String shopcode) {
        this.shopcode = shopcode;
    }

    public void setShopcommison(double shopcommison) {
        this.shopcommison = shopcommison;
    }

    public void setShopid(int shopid) {
        this.shopid = shopid;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public void setShopwalletbalance(double shopwalletbalance) {
        this.shopwalletbalance = shopwalletbalance;
    }

    public void setSubagentcommmision(double subagentcommmision) {
        this.subagentcommmision = subagentcommmision;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    public void setWalletbalance(double walletbalance) {
        this.walletbalance = walletbalance;
    }

}
