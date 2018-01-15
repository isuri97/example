package org.wso2.carbon.identity.oauth.uma.service.exceptions;

/**
 * Created by isuri on 1/10/18.
 */
public class UMAClientException extends UMAException {

    public UMAClientException(String message) {

        super(message);
    }

    public UMAClientException(String message, Throwable throwable) {

        super(message, throwable);
    }

    public UMAClientException(int stausCode, String errorMessage) {

        super(errorMessage);
        this.setStatusCode(stausCode);
        this.setErrorDescription(errorMessage);
    }

    public UMAClientException(int statusCode, String errorcode, String errorMessage) {

        super(errorMessage);
        this.setErrorCode(errorcode);
        this.setErrorDescription(errorMessage);
        this.setStatusCode(statusCode);
    }

    public UMAClientException(String statusCode, String errorcode, String errorMessage, Throwable throwable) {

        super(errorMessage, throwable);
        this.setErrorCode(errorcode);
        this.setErrorDescription(errorMessage);

    }
}

