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
import org.wso2.carbon.identity.oauth.uma.endpoint.impl.util.ResourceUtils;
import org.wso2.carbon.identity.oauth.uma.service.ResourceConstants;

import org.wso2.carbon.identity.oauth.uma.service.exceptions.UmaEndpointLayerException;
import org.wso2.carbon.identity.oauth.uma.service.exceptions.UmaException;
import org.wso2.carbon.identity.oauth.uma.service.exceptions.UmaServerException;
import org.wso2.carbon.identity.oauth.uma.service.model.ResourceRegistration;

import java.util.List;
import javax.ws.rs.core.Response;

/**
 * ResourceRegistrationApiServiceImpl is used to handling resource management.
 */
public class ResourceRegistrationApiServiceImpl extends ResourceRegistrationApiService {

    private static final Log LOG = LogFactory.getLog(ResourceRegistrationApiServiceImpl.class);
    private static Response.Status status;
    private static ErrorDTO errorDTO = new ErrorDTO();


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
        ResourceRegistration registerResource;

        if (requestedResource == null) {
            LOG.error("Request body cannot be empty.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            registerResource = ResourceUtils.getResourceService()
                    .registerResourceSet(ResourceUtils.getResource(requestedResource));
            createResourceDTO = ResourceUtils.createResponse(registerResource);
            response = Response.ok().entity(createResourceDTO).build();
        } catch (UmaEndpointLayerException e) {
            handleErrorResponse(e);
            LOG.error("Invalid request.Request valid resource Id to delete the resource. ", e);
            return Response.status(status).entity(errorDTO).build();

        } catch (UmaServerException e) {
            handleErrorResponse(e);
            LOG.error("Invalid request. ", e);
            return Response.serverError().build();

        } catch (Throwable throwable) {
            LOG.error("Internal server error occurred. ", throwable);
            return Response.serverError().build();
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
        ResourceRegistration resourceRegistration;
        ReadResourceDTO readResourceDTO;

        try {
            resourceRegistration = ResourceUtils.getResourceService().getResourceSetById(resourceId);
            readResourceDTO = ResourceUtils.readResponse(resourceRegistration);
            response = Response.ok().entity(readResourceDTO).build();
        } catch (UmaEndpointLayerException e) {
            handleErrorResponse(e);
            LOG.error("Invalid request.Request with valid resource Id to update the resource. ", e);
            return Response.status(status).entity(errorDTO).build();

        } catch (Throwable throwable) {
            LOG.error("Internal server error occurred. ", throwable);
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
        Response response = null;
        List<String> resourceRegistration = null;
        if (resourceOwnerId != "123") {
            try {
                resourceRegistration = ResourceUtils.getResourceService().getResourceSetIds(resourceOwnerId);
                response = Response.ok().entity(resourceRegistration).build();
            } catch (UmaEndpointLayerException e) {
                handleErrorResponse(e);
                LOG.error("Invalid request.Request with valid resource Id to update the resource. ", e);
                return Response.status(status).entity(errorDTO).build();

            } catch (UmaException e) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return response;
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

        ResourceRegistration resourceRegistration;
        Response response = null;
        //ReadResourceDTO resourceDetailsDTO;
        try {
            resourceRegistration = ResourceUtils.getResourceService().updateResourceSet(resourceId, ResourceUtils.
                    getResource(updatedResource));
            response = Response.ok().entity(updatedResource).build();
        } catch (UmaEndpointLayerException e) {
            handleErrorResponse(e);
            LOG.error("Invalid request.Request with valid resource Id to update the resource. ", e);
            return Response.status(status).entity(errorDTO).build();
        } catch (UmaServerException e) {
            handleErrorResponse(e);
            LOG.error("Invalid request. ", e);
            return Response.serverError().build();

        } catch (Throwable throwable) {
            LOG.error("Internal server error occurred. ", throwable);
            return Response.serverError().build();
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
        } catch (UmaEndpointLayerException e) {
            handleErrorResponse(e);
            LOG.error("Invalid request.Request valid resource Id to delete the resource. ", e);
            return Response.status(status).entity(errorDTO).build();

        } catch (UmaServerException e) {
            handleErrorResponse(e);
            LOG.error("Invalid request. ", e);
            return Response.serverError().build();

        } catch (Throwable throwable) {
            LOG.error("Internal server error occurred. ", throwable);
            return Response.serverError().build();
        }
        return response;
    }


    public static void handleErrorResponse(UmaException umaException) {

        ResourceConstants resourceConstants = new ResourceConstants();
        int statusCode = umaException.getStatusCode();
        status = Response.Status.fromStatusCode(statusCode);
        errorDTO.setCode(resourceConstants.getCode());
        errorDTO.setDescription(resourceConstants.getMap().get(statusCode).getMessage());

    }
}
