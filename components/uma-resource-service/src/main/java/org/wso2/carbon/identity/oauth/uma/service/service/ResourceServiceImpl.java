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

package org.wso2.carbon.identity.oauth.uma.service.service;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.oauth.uma.service.ResourceService;
import org.wso2.carbon.identity.oauth.uma.service.dao.ResourceDAO;
import org.wso2.carbon.identity.oauth.uma.service.exceptions.UMAClientException;
import org.wso2.carbon.identity.oauth.uma.service.exceptions.UMAException;
import org.wso2.carbon.identity.oauth.uma.service.exceptions.UMAServiceException;
import org.wso2.carbon.identity.oauth.uma.service.model.ResourceRegistration;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ResourceService use for resource management
 */
public class ResourceServiceImpl implements ResourceService {

    private static final Log log = LogFactory.getLog(ResourceServiceImpl.class);
    ResourceDAO resourceDAO = new ResourceDAO();

    @Override
    public ResourceRegistration registerResourceSet(ResourceRegistration resourceRegistration) throws
            UMAException {
        // check whether the resource id is provided
        if (StringUtils.isBlank(resourceRegistration.getName())) {
            String errorMessage = "Resource name can not be null.";
            throw new UMAClientException(404, errorMessage);
        } else {
            try {

                resourceRegistration = resourceDAO.registerResourceSet(resourceRegistration);
            } catch (UMAServiceException e) {
                String errorMessage = "Resource id is not persistant on db.";
                throw new UMAClientException(404, "Resource id not found", errorMessage);
            }

            return resourceRegistration;
        }
    }

    /**
     * Retrieve the available Resource list
     *
     * @param resourceOwnerId To ientify resources belongs to same owner
     * @return resource list
     * @throws UMAException
     */
    @Override
    public List<String> getResourceSetIds(String resourceOwnerId) throws UMAException {
        // return resourceDAO.retrieveResourceIDs(accessToken);

        List<String> resourceRegistration = null;


        try {
            resourceRegistration = resourceDAO.retrieveResourceIDs(resourceOwnerId);

        } catch (UMAServiceException e) {
            String errorMessage = "Resource Owner id is not persistant on db.";
            throw new UMAClientException(404, "Resource List not found", errorMessage);
        }

        return resourceRegistration;
    }


    /**
     * @param resourceid resource ID of the resource which need to get retrieved
     * @return Retrieved resource using resource ID
     * @throws UMAException
     */
    @Override
    public ResourceRegistration getResourceSetById(String resourceid)
            throws UMAServiceException, UMAClientException {


        ResourceRegistration resourceRegistration = null;

        if (isResourceId(resourceid)) {
            String errorMessage = "Resource id is not in defined format.";
            throw new UMAClientException(400, "invalid_resource_id", errorMessage);

        } else {
            try {
                resourceRegistration = resourceDAO.retrieveResourceset(resourceid);
            } catch (UMAServiceException e) {
                String errorMessage = "Resource id is not persistant on db.";
                throw new UMAClientException(404, "Resource id not found", errorMessage);
            }
            return resourceRegistration;

        }
    }

    /**
     * Update the resource of the given resource ID
     *
     * @param resourceRegistration details of updated resource
     * @return updated resource
     * @throws UMAException
     */
    @Override
    public ResourceRegistration updateResourceSet(String resourceid, ResourceRegistration resourceRegistration)
            throws SQLException, UMAException {


        if (isResourceId(resourceid)) {
            String errorMessage = "Resource id is not in defined format.";
            throw new UMAClientException(400, "invalid_resource_id", errorMessage);

        } else {
            try {
                resourceDAO.updateResourceSet(resourceid, resourceRegistration);
            } catch (UMAServiceException e) {
                String errorMessage = "Resource id is not persistant on db.";
                throw new UMAClientException(404, "Resource id not found", errorMessage);
            }
            return resourceRegistration;
        }
    }

    /**
     * Delete the resource for the given resource ID
     *
     * @param resourceid Resource ID of the resource which need to get deleted
     * @throws UMAException
     */
    @Override
    public boolean deleteResourceSet(String resourceid) throws UMAException, SQLException {

        ResourceRegistration resourceRegistration = null;

        if (isResourceId(resourceid)) {
            String errorMessage = "Resource id is not in defined format.";
            throw new UMAClientException(400, "invalid_resource_id", errorMessage);

        } else {
            try {
                resourceDAO.deleteResourceSet(resourceid);
            } catch (UMAServiceException e) {
                String errorMessage = "Resource id is not persistant on db.";
                throw new UMAClientException(404, "Resource id not found", errorMessage);
            }
        }

        return false;
    }


    public boolean isResourceId(String resourceId) {

        boolean validator = true;
        String pattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
        Pattern pat = Pattern.compile(pattern);
        Matcher match = pat.matcher(resourceId);

        if (match.find()) {
            validator = false;
        }
        return validator;
    }
}
