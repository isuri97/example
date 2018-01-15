package org.wso2.carbon.identity.oauth.uma.service.exceptions;


/**
 * Created by isuri on 1/10/18.
 */
public class UMAServerException extends UMAException {

    public UMAServerException(String errorDescription) {

        super(errorDescription);
    }

    public UMAServerException(String errorDescription, Throwable cause) {

        super(errorDescription, cause);
    }

    public UMAServerException(String errorCode, String message) {

        super(errorCode, message);
    }

    public UMAServerException(String errorCode, String message, Throwable throwable) {

        super(errorCode, message, throwable);
    }
}
