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

package org.wso2.carbon.identity.oauth.uma.endpoint.impl.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.oauth.uma.endpoint.ResourceRegistrationApiService;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.CreateResourceDTO;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.ErrorDTO;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.ReadResourceDTO;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.ResourceDetailsDTO;
import org.wso2.carbon.identity.oauth.uma.endpoint.impl.exceptions.ResourceEndpointException;
import org.wso2.carbon.identity.oauth.uma.endpoint.impl.util.ResourceUtils;
import org.wso2.carbon.identity.oauth.uma.service.ResourceConstants;

import org.wso2.carbon.identity.oauth.uma.service.exceptions.UMAClientException;
import org.wso2.carbon.identity.oauth.uma.service.exceptions.UMAException;
import org.wso2.carbon.identity.oauth.uma.service.model.ResourceRegistation;

import java.util.List;
import javax.ws.rs.core.Response;

/**
 * ResourceRegistrationApiServiceImpl is used to handling resource management.
 */
public class ResourceRegistrationApiServiceImpl extends ResourceRegistrationApiService {

    private static final Log log = LogFactory.getLog(ResourceRegistrationApiServiceImpl.class);
    private static Response.Status status;


    /**
     * Register a resource with resource details
     *
     * @param requestedResource details of the resource to be registered
     * @return Response with the status of the registration
     */
    @Override
    public Response registerResource(ResourceDetailsDTO requestedResource) {

        CreateResourceDTO createResourceDTO;
        Response response = null;
        ResourceRegistation registerResource;

        if (requestedResource == null) {
            log.error("Request body cannot be empty.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            registerResource = ResourceUtils.getResourceService()
                    .registerResourceSet(ResourceUtils.getResource(requestedResource));
            createResourceDTO = ResourceUtils.createResponse(registerResource);
            response = Response.ok().entity(createResourceDTO).build();
        } catch (UMAClientException e) {
            handleErrorResponse(e,log);
            //Log.info("Invalid request.Request valid resource Id to delete the resource. ", e);
           /* return Response.status(status).entity(errorDTO).build();*/

        /*} catch (UMAServerException e) {
            handleErrorResponse(e,log);
            log.error("Invalid request. ", e);
           *//* return Response.serverError().build();*//*
*/
        } catch (Throwable throwable) {
            log.error("Internal server error occurred. ", throwable);
            /*return Response.serverError().build();*/
        }

        return response;
    }

    /**
     * Retrieve the resource of the given resourceId
     *
     * @param resourceId Resourceid of the resource which need to get retrieved
     * @return Response with the retrieved resource/ retrieval status
     */
    @Override
    public Response getResource(String resourceId) {

        Response response = null;
        ResourceRegistation resourceRegistration;
        ReadResourceDTO readResourceDTO;

        try {
            resourceRegistration = ResourceUtils.getResourceService().getResourceSetById(resourceId);
            readResourceDTO = ResourceUtils.readResponse(resourceRegistration);
            response = Response.ok().entity(readResourceDTO).build();
        } catch (UMAClientException e) {
            handleErrorResponse(e,log);
            log.error("Invalid request.Request with valid resource Id to update the resource. ", e);
            //return Response.status(status).entity(errorDTO).build();

        } catch (Throwable throwable) {
            log.error("Internal server error occurred. ", throwable);
            return Response.serverError().build();
        }
        return response;
    }


    /**
     * Retrieve the available resourceId list
     *
     * @param resourceOwnerId
     * @return Response with the retrieved resourceId's/ retrieval status
     */
    @Override
    public Response getresourceIds(String resourceOwnerId) {
        // For testing purpose current resource owner id is taken to validate pat access token

        resourceOwnerId = "123";
        try {
            List<String> resourceRegistration = ResourceUtils.getResourceService().getResourceSetIds(resourceOwnerId);
            Response response = Response.ok().entity(resourceRegistration).build();
            return response;
        } catch (UMAClientException e) {
            handleErrorResponse(e, log);
            log.error("Invalid request.Request with valid resource Id to update the resource. ", e);
            //return Response.status(status).entity(errorDTO).build();

        } catch (UMAException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return null;
    }


    /**
     * Update a resource
     *
     * @param updatedResource details of the resource to be updated
     * @param resourceId      ID of the resource to be updated
     * @return
     */
    @Override
    public Response updateResource(String resourceId, ResourceDetailsDTO updatedResource) {

        ResourceRegistation resourceRegistration;
        Response response = null;
        //ReadResourceDTO resourceDetailsDTO;
        try {
            resourceRegistration = ResourceUtils.getResourceService().updateResourceSet(resourceId, ResourceUtils.
                    getResource(updatedResource));
            response = Response.ok().entity(updatedResource).build();
        } catch (UMAClientException e) {
            handleErrorResponse(e,log);
            log.error("Invalid request.Request with valid resource Id to update the resource. ", e);
            /*return Response.status(status).entity(errorDTO).build();*/

        } catch (Throwable throwable) {
            log.error("Internal server error occurred. ", throwable);
            /*return Response.serverError().build();*/
        }
        return response;
    }

    /**
     * Delete the resource for the given resourceId
     *
     * @param resourceId resourceId of the resource which need to get deleted
     * @return Response with the status of resource deletion
     */
    @Override
    public Response deleteResource(String resourceId) {
        // CreateResourceDTO createResourceDTO;
        Response response = null;
        // if (!isResourceId(resourceId)) {

        try {
            ResourceUtils.getResourceService().deleteResourceSet(resourceId);
        } catch (UMAClientException e) {
            handleErrorResponse(e,log);
            log.error("Invalid request.Request valid resource Id to delete the resource. ", e);
           // return Response.status(status).entity(errorDTO).build();

        /*} catch (UMAServerException e) {
            handleErrorResponse(e,log);
            log.error("Invalid request. ", e);
            //return Response.serverError().build();
*/
        } catch (Throwable throwable) {
            log.error("Internal server error occurred. ", throwable);
            //return Response.serverError().build();
        }
        return response;
    }


 /*   public ErrorDTO handleErrorResponse(UMAException umaException) {

        ResourceConstants resourceConstants = new ResourceConstants();
        ErrorDTO errorDTO = new ErrorDTO();
        int statusCode = umaException.getStatusCode();
        Response.Status status = Response.Status.fromStatusCode(statusCode);
        errorDTO.setCode(resourceConstants.getCode());
        errorDTO.setDescription(resourceConstants.getMap().get(statusCode).getMessage());
        return errorDTO;

    }*/

    private static void handleErrorResponse(UMAException umaException, Log log) throws ResourceEndpointException {

        String code = umaException.getErrorCode();

        String statusCode;
        String errorCode = null;
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        boolean isStatusOnly = true;

        if (code != null) {
           /* org.wso2.carbon.identity.oauth.uma.endpoint.impl.impl.ResourceEndpointConstants;
            if (org.wso2.carbon.identity.oauth.uma.endpoint.PermissionEndpointConstants.responseDataMap.containsKey(code)) {
                statusCode = org.wso2.carbon.identity.oauth.uma.endpoint.PermissionEndpointConstants.responseDataMap.get(code)[0];
                errorCode = org.wso2.carbon.identity.oauth.uma.endpoint.PermissionEndpointConstants.responseDataMap.get(code)[1];
                status = Response.Status.fromStatusCode(Integer.parseInt(statusCode));
                isStatusOnly = false;*/
            }
        }


    private static void handleErrorResponse(Response.Status status, Throwable throwable,
                                            boolean isServerException, Log log)
            throws ResourceEndpointException {

        String code;
        if (throwable instanceof UMAException) {
            code = ((UMAException) throwable).getErrorCode();
        } else {
            code = ResourceConstants.ErrorMessages.ERROR_UNEXPECTED.getCode();
        }

        if (isServerException) {
            if (throwable == null) {
                log.error(status.getReasonPhrase());
            } else {
                log.error(status.getReasonPhrase(), throwable);
            }
        }
        throw buildResourceEndpointException(status, code, throwable == null ? "" : throwable.getMessage(),
                isServerException);
    }

    private static ResourceEndpointException buildResourceEndpointException(Response.Status status,
                                                                                String errorCode, String description,
                                                                                boolean isStatus) {
        if (isStatus) {
            return new ResourceEndpointException(status);
        } else {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setCode(errorCode);
            errorDTO.setDescription(description);
            return new ResourceEndpointException(status, errorDTO);
        }
    }

}



