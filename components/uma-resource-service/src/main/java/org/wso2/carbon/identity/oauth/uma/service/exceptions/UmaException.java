package org.wso2.carbon.identity.oauth.uma.service.exceptions;

/**
 * Created by isuri on 1/10/18.
 */
public class UmaException extends Exception {

    private String errorCode;
    private String errorDescription;
    private int statusCode;


    public UmaException(String message) {

        super(message);
    }

    public UmaException(String message, Throwable throwable) {

        super(message, throwable);
    }

    public UmaException(String errorcode, String message) {

        super(message);
        this.errorCode = errorcode;
    }

    public UmaException(String errorCode, String message, Throwable throwable) {

        super(message, throwable);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {

        return errorCode;
    }

    public void setErrorCode(String errorCode) {

        this.errorCode = errorCode;
    }

    public String getErrorDescription() {

        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {

        this.errorDescription = errorDescription;
    }

    public int getStatusCode() {

        return statusCode;
    }

    public void setStatusCode(int statusCode) {

        this.statusCode = statusCode;
    }
}
