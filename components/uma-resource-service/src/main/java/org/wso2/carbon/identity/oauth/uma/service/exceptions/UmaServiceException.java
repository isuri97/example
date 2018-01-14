package org.wso2.carbon.identity.oauth.uma.service.exceptions;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.oauth.uma.service.ResourceConstants;

/**
 * Created by isuri on 1/10/18.
 */
public class UmaServiceException extends UmaException {

    public UmaServiceException(String message) {

        super(message);
    }

    public UmaServiceException(ResourceConstants.ErrorMessages message1, String throwable) {

        super(message1.getMessage(), throwable);
    }

    public UmaServiceException(String errorcode, String message) {

        super(errorcode, message);
    }

    public UmaServiceException(String errorCode, Throwable throwable, String message) {

        super(errorCode, message, throwable);
    }

    public String getErrorDescription() {

        String errorDescription = this.getMessage();
        if (StringUtils.isEmpty(errorDescription)) {
            errorDescription = ResourceConstants.ErrorMessages.ERROR_CODE_UNEXPECTED.getMessage();
        }
        return errorDescription;
    }

    private String getDefaultErrorCode() {

        String errorCode = super.getErrorCode();
        if (StringUtils.isEmpty(errorCode)) {
            errorCode = ResourceConstants.ErrorMessages.ERROR_CODE_UNEXPECTED.getCode();
        }
        return errorCode;
    }
}
