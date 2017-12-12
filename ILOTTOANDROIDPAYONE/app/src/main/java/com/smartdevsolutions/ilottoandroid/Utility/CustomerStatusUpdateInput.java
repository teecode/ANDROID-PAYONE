package com.smartdevsolutions.ilottoandroid.Utility;

/**
 * Created by BINARYCODES on 5/6/2017.
 */

public class CustomerStatusUpdateInput {
    private int custId ;
    private int postId ;
    private int status ;

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
