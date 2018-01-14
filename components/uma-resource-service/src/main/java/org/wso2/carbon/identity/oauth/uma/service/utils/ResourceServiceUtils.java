/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.carbon.identity.oauth.uma.service.utils;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.oauth.uma.service.ResourceConstants;
import org.wso2.carbon.identity.oauth.uma.service.exceptions.ResourceServerException;
import org.wso2.carbon.identity.oauth.uma.service.exceptions.ResourceServiceClientException;

/**
 * Utility methods for resource registration implementation
 */
public class ResourceServiceUtils {

    public static ResourceServerException generateServerException(ResourceConstants.ErrorMessages
                                                                          error, String data)
            throws ResourceServerException {

        String errorDescription;
        if (StringUtils.isNotBlank(data)) {
            errorDescription = String.format(error.getMessage(), data);
        } else {
            errorDescription = error.getMessage();
        }

        return IdentityException.error(
                ResourceServerException.class, error.getCode(), errorDescription);
    }

    public static ResourceServerException generateServerException(ResourceConstants.ErrorMessages
                                                                          error, String data, Throwable e)
            throws ResourceServerException {

        String errorDescription;
        if (StringUtils.isNotBlank(data)) {
            errorDescription = String.format(error.getMessage(), data);
        } else {
            errorDescription = error.getMessage();
        }

        return IdentityException.error(
                ResourceServerException.class, error.getCode(), errorDescription, e);
    }

    public static ResourceServerException generateServerException(ResourceConstants.ErrorMessages
                                                                          error, Throwable e)
            throws ResourceServerException {

        return IdentityException.error(
                ResourceServerException.class, error.getCode(), error.getMessage(), e);
    }

    public static ResourceServiceClientException generateClientException(ResourceConstants.ErrorMessages
                                                                                 error, String data)
            throws ResourceServiceClientException {

        String errorDescription;
        if (StringUtils.isNotBlank(data)) {
            errorDescription = String.format(error.getMessage(), data);
        } else {
            errorDescription = error.getMessage();
        }

        return IdentityException.error(ResourceServiceClientException.class, error.getCode(), errorDescription);
    }

    public static ResourceServiceClientException generateClientException(ResourceConstants.ErrorMessages error,
                                                                         String data,
                                                                         Throwable e)
            throws ResourceServiceClientException {

        String errorDescription;
        if (StringUtils.isNotBlank(data)) {
            errorDescription = String.format(error.getMessage(), data);
        } else {
            errorDescription = error.getMessage();
        }

        return IdentityException.error(ResourceServiceClientException.class, error.getCode(), errorDescription, e);
    }

    public static int getTenantID() {
        return PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
    }
}
