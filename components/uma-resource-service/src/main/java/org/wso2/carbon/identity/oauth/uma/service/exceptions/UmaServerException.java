package org.wso2.carbon.identity.oauth.uma.service.exceptions;


/**
 * Created by isuri on 1/10/18.
 */
public class UmaServerException extends UmaException {

    public UmaServerException(String errorDescription) {

        super(errorDescription);
    }

    public UmaServerException(String errorDescription, Throwable cause) {

        super(errorDescription, cause);
    }

    public UmaServerException(String errorCode, String message) {

        super(errorCode, message);
    }

    public UmaServerException(String errorCode, String message, Throwable throwable) {

        super(errorCode, message, throwable);
    }
}
