package com.smartdevsolutions.ilottoandroid.ApiResource;

/**
 * Created by teecodez on 10/27/2018.
 */

public class BadRequestResource {
    private String ErrorCode;
    private String ErrorMessage;

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}
