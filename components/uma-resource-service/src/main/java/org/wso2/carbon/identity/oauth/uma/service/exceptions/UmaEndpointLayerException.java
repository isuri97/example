package org.wso2.carbon.identity.oauth.uma.service.exceptions;

/**
 * Created by isuri on 1/10/18.
 */
public class UmaEndpointLayerException extends UmaException {

    public UmaEndpointLayerException(String message) {

        super(message);
    }

    public UmaEndpointLayerException(String message, Throwable throwable) {

        super(message, throwable);
    }

    public UmaEndpointLayerException(int stausCode, String errorMessage) {

        super(errorMessage);
        this.setStatusCode(stausCode);
        this.setErrorDescription(errorMessage);
    }

    public UmaEndpointLayerException(int statusCode, String errorcode, String errorMessage) {

        super(errorMessage);
        this.setErrorCode(errorcode);
        this.setErrorDescription(errorMessage);
        this.setStatusCode(statusCode);
    }

    public UmaEndpointLayerException(String statusCode, String errorcode, String errorMessage, Throwable throwable) {

        super(errorMessage, throwable);
        this.setErrorCode(errorcode);
        this.setErrorDescription(errorMessage);

    }
}

