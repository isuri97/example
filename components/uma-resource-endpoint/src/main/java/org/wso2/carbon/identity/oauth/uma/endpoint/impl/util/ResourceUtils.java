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

package org.wso2.carbon.identity.oauth.uma.endpoint.impl.util;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.CreateResourceDTO;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.ListReadResourceDTO;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.ReadResourceDTO;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.ResourceDetailsDTO;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.UpdateResourceDTO;
import org.wso2.carbon.identity.oauth.uma.service.ResourceService;
import org.wso2.carbon.identity.oauth.uma.service.model.ResourceRegistration;


/**
 * This class holds the util methods used by ResourceRegistrationApiServiceImpl.
 */
public class ResourceUtils {

    public static ResourceService getResourceService() {

        return (ResourceService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(ResourceService.class, null);
    }


    /**
     * Logs the error, builds a ScopeEndpointException with specified details and throws it
     *
     * @param status    response status
     * @param message   error message
     * @param throwable throwable
     * @throws ResourceEndpointException
     */
    /*public static void handleErrorResponse(Response.Status status, String message, Throwable throwable,
                                           boolean isServerException, Log log)
            throws ResourceEndpointException {

        String errorCode;
        if (throwable instanceof ResourceException) {
            errorCode = ((ResourceException) throwable).toString();
        } else {
            errorCode = ResourceConstants.ErrorMessages.ERROR_CODE_UNEXPECTED.getCode();
        }

        if (isServerException) {
            if (throwable == null) {
                log.error(message);
            } else {
                log.error(message, throwable);
            }
        }
        throw buildResourceEndpointException(status, message, errorCode,
                throwable == null ? "" : throwable.getMessage(),
                isServerException);
    }

    public static ResourceEndpointException buildResourceEndpointException(Response.Status status, String message,
                                                                            String code, String description,
                                                                            boolean isServerException) {

        ErrorDTO errorDTO = getErrorDTO(message, code, description);
        if (isServerException) {
            return new ResourceEndpointException(status);
        } else {
            return new ResourceEndpointException(status, errorDTO);
        }
    }

    *//**
     * Returns a generic errorDTO
     *
     * @return A generic errorDTO with the specified details
     *//*
    public static ErrorDTO getErrorDTO(String message, String code, String description) {

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCode(code);
        errorDTO.setMessage(message);
        errorDTO.setDescription(description);
        return errorDTO;
    }
*/
/*
    public static void handleErrorResponse(UMAException umaException) {


        ResourceConstants resourceConstants = new ResourceConstants();
        int statusCode = umaException.getStatusCode();
        status = Response.Status.fromStatusCode(statusCode);
        errorDTO.setCode(resourceConstants.getCode());
        errorDTO.setDescription(resourceConstants.getMap().get(statusCode).getMessage());

    }*/


    /**
     * Returns a generic resourceDetailsDTO
     *
     * @param resourceRegistration specifies the details carried out by the ResourceRegistration
     * @return A generic resourceDetailsDTO details
     */
    public static ResourceDetailsDTO getResourceDTO(ResourceRegistration resourceRegistration) {

        ResourceDetailsDTO resourceDetailsDTO = new ResourceDetailsDTO();
        resourceDetailsDTO.setName(resourceDetailsDTO.getName());
        resourceDetailsDTO.setResource_scopes(resourceDetailsDTO.getResource_scopes());
        resourceDetailsDTO.setIcon_uri(resourceDetailsDTO.getIcon_uri());
        resourceDetailsDTO.setDescription(resourceDetailsDTO.getDescription());
        resourceDetailsDTO.setType(resourceDetailsDTO.getType());
        return resourceDetailsDTO;
    }


    /**
     * Returns a resourceRegistration object
     *
     * @param resourceDetailsDTO specifies the details carried out by the ResourceDetailsDTO
     * @return A generic resourceregistration with the specified details
     */
    public static ResourceRegistration getResource(ResourceDetailsDTO resourceDetailsDTO) {

        ResourceRegistration resourceRegistration = new ResourceRegistration();
        resourceRegistration.setName(resourceDetailsDTO.getName());
        resourceRegistration.setScopes(resourceDetailsDTO.getResource_scopes());
        if (resourceDetailsDTO.getIcon_uri() != null) {
            resourceRegistration.setIcon_uri("icon_uri", resourceDetailsDTO.getIcon_uri());
            resourceRegistration.getPropertyData().add(resourceRegistration.getIconuri());
        }
        if (resourceDetailsDTO.getType() != null) {
            resourceRegistration.setType("type", resourceDetailsDTO.getType());
            resourceRegistration.getPropertyData().add(resourceRegistration.getType());
        }
        if (resourceDetailsDTO.getDescription() != null) {
            resourceRegistration.setDescription("description", resourceDetailsDTO.getDescription());
            resourceRegistration.getPropertyData().add(resourceRegistration.getDescription());
        }
        return resourceRegistration;
    }

    /**
     * Returns a ReadResourceDTO object
     *
     * @param resourceRegistration specifies the details carried out by the ResourceRegistration Model class
     * @return A generic readresourceDTO with the specified details
     */
    public static ReadResourceDTO readResponse(ResourceRegistration resourceRegistration) {

        ReadResourceDTO readResourceDTO = new ReadResourceDTO();
        readResourceDTO.setResourceId(resourceRegistration.getResourceId());
        readResourceDTO.setName(resourceRegistration.getName());
        readResourceDTO.setType(resourceRegistration.getType().getData());
        readResourceDTO.setDescription(resourceRegistration.getDescription().getData());
        readResourceDTO.setIcon_uri(resourceRegistration.getIconuri().getData());
        readResourceDTO.setResource_scope(resourceRegistration.getScopes());
        return readResourceDTO;
    }

    /**
     * Returns a CreateResourceDTO object
     *
     * @param resourceRegistration specifies the details carried out by the ResourceRegistration Model class
     * @return A generic createResourceDTO with the specified details
     */
    public static CreateResourceDTO createResponse(ResourceRegistration resourceRegistration) {

        CreateResourceDTO createResourceDTO = new CreateResourceDTO();

        createResourceDTO.setResourceId(resourceRegistration.getResourceId());
        return createResourceDTO;
    }

    /**
     * Returns a UpdateResourceDTO object
     *
     * @param resourceRegistration specifies the details carried out by the ResourceRegistration Model class
     * @return A generic updateResourceDTO with the specified details
     */
    public static UpdateResourceDTO updateResponse(ResourceRegistration resourceRegistration) {

        UpdateResourceDTO updateResourceDTO = new UpdateResourceDTO();
        //updateResourceDTO.setResourceId(resourceRegistration.getResourceId());
        return updateResourceDTO;
    }

    public static ListReadResourceDTO listResourceId(ResourceRegistration resourceRegistration) {

        ListReadResourceDTO listReadResourceDTO = new ListReadResourceDTO();
        // listReadResourceDTO.setResourceId(resourceRegistration.getResourceId());
        return listReadResourceDTO;
    }
}
