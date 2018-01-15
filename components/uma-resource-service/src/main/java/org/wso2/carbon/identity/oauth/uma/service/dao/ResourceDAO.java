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

package org.wso2.carbon.identity.oauth.uma.service.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.core.persistence.JDBCPersistenceManager;
import org.wso2.carbon.identity.oauth.uma.service.ResourceConstants;
import org.wso2.carbon.identity.oauth.uma.service.exceptions.UMAServiceException;
import org.wso2.carbon.identity.oauth.uma.service.model.MetaData;
import org.wso2.carbon.identity.oauth.uma.service.model.ResourceRegistration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Layer functionality for Resource management. This includes storing, updating, deleting
 * and retrieving resources.
 */
public class ResourceDAO {

    private static final Log log = LogFactory.getLog(ResourceDAO.class);

    /**
     * Add a resource
     *
     * @param resourceRegistration details of the registered resource
     * @return resourceId of resgistered resource description
     * @throws UMAServiceException ResourceException
     */
    public ResourceRegistration registerResourceSet(ResourceRegistration resourceRegistration)
            throws UMAServiceException {

        String resourcesql = SQLQueries.INSERT_RESOURCE;
        String metadatasql = SQLQueries.INSERT_INTO_RESOURCE_META_DATA;
        String scopesql = SQLQueries.INSERT_INTO_RESOURCE_SCOPE1;

        try (Connection connection = JDBCPersistenceManager.getInstance().getDBConnection()) {

            try (PreparedStatement preparedStatement = connection.prepareStatement(resourcesql)) {
                preparedStatement.setString(1, resourceRegistration.getResourceId());
                preparedStatement.setString(2, resourceRegistration.getName());
                preparedStatement.setTimestamp(3, resourceRegistration.getTimecreated());
                preparedStatement.setString(4, resourceRegistration.getResourceOwnerId());
                preparedStatement.setString(5, resourceRegistration.getTenentId());
                preparedStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(metadatasql)) {
                preparedStatement.setString(1, resourceRegistration.getResourceId());
                for (MetaData metaData : resourceRegistration.getPropertyData()) {
                    preparedStatement.setString(2, metaData.getKey());
                    preparedStatement.setString(3, metaData.getData());
                    preparedStatement.execute();
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(scopesql)) {
                preparedStatement.setString(1, resourceRegistration.getResourceId());
                preparedStatement.setString(2, String.valueOf(resourceRegistration.getScopes()));
                preparedStatement.execute();
                connection.commit();
            }

        } catch (SQLException e) {
            log.error("Error when retrieving the resource description. ");
            String errordescription = "Resource Id can not be found in data base";
            throw new UMAServiceException(ResourceConstants.ErrorMessages.ERROR_CODE_FAIL_TO_GET_RESOURCE,
                    errordescription);
        }
        return resourceRegistration;
    }

    /**
     * Get a resource by resourceId
     *
     * @param resourceid Id of the resource
     * @return resource description for the provided ID
     * @throws UMAServiceException
     */
    public ResourceRegistration retrieveResourceset(String resourceid) throws UMAServiceException {

        String sql = SQLQueries.GET_ALL_RESOURCE_FROM_ID;
        ResourceRegistration resourceRegistration = new ResourceRegistration();

        try (Connection connection = JDBCPersistenceManager.getInstance().getDBConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, resourceid);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (!resultSet.next()) {
                        return null;
                    } else {
                        if (resultSet.first()) {
                            if (resultSet.getString(5) != null) {
                                String scopeResult = resultSet.getString(5);
                                if (!resourceRegistration.getScopes().contains(scopeResult)) {
                                    for (String split : scopeResult.split(",")) {
                                        resourceRegistration.getScopes().add(split);
                                    }
                                }
                            }
                        }


                        resultSet.beforeFirst();
                        while (resultSet.next()) {

                            if (resultSet.getString(1) != null) {
                                resourceRegistration.setResourceId(resultSet.getString(1));
                            }
                            if (resultSet.getString(2) != null) {
                                resourceRegistration.setName(resultSet.getString(2));
                            }
                            if (resultSet.getString("PROPERTY_KEY").equals("icon_uri")) {
                                resourceRegistration.setIcon_uri(resultSet.getString("PROPERTY_KEY"),
                                        resultSet.getString("PROPERTY_VALUE"));

                            }
                            if (resultSet.getString("PROPERTY_KEY").equals("type")) {
                                resourceRegistration.setType(resultSet.getString("PROPERTY_KEY"),
                                        resultSet.getString("PROPERTY_VALUE"));
                            }
                            if (resultSet.getString("PROPERTY_KEY").equals("description")) {
                                resourceRegistration.setDescription(resultSet.getString("PROPERTY_KEY"),
                                        resultSet.getString("PROPERTY_VALUE"));
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error when retrieving the resource description. ");
            String errordescription = "Resource Id can not be found in data base";
            throw new UMAServiceException(ResourceConstants.ErrorMessages.ERROR_CODE_FAIL_TO_GET_RESOURCE,
                    errordescription);

        }
        return resourceRegistration;
    }

    /**
     * Get all available resources
     *
     * @param resourceOwnerId ResourceOwner ID
     * @return available resource list
     * @throws UMAServiceException
     */
    public List<String> retrieveResourceIDs(String resourceOwnerId) throws UMAServiceException {

        List<String> resourceSetIdList = new ArrayList<>();
        String sql = SQLQueries.GET_RESOURCE_IDS;
        try (Connection connection = JDBCPersistenceManager.getInstance().getDBConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, resourceOwnerId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        String resourceId = resultSet.getString(1);
                        resourceSetIdList.add(resourceId);
                    }
                }
            }
            connection.commit();
        } catch (SQLException e) {
            log.error("Error when retrieving the resource description. ");
            String errordescription = "Resource Id can not be found in data base";
            throw new UMAServiceException(ResourceConstants.ErrorMessages.ERROR_CODE_FAIL_TO_GET_RESOURCE,
                    errordescription);
        }
        return resourceSetIdList;
    }

    /**
     * Delete a resource description of the provided resource ID
     *
     * @param resourceid Resource ID of the resource
     * @throws UMAServiceException
     */
    public boolean deleteResourceSet(String resourceid) throws SQLException, UMAServiceException {

        String sql = SQLQueries.DELETE_RESOURCE_BY_ID;

        try (Connection connection = JDBCPersistenceManager.getInstance().getDBConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, resourceid);
                int rowsAffected = preparedStatement.executeUpdate();
                connection.commit();
                return rowsAffected > 0;
            } catch (SQLException e) {
                log.error("Error when retrieving the resource description. ");
                String errordescription = "Resource Id can not be found in data base";
                throw new UMAServiceException(ResourceConstants.ErrorMessages.ERROR_CODE_FAIL_TO_GET_RESOURCE,
                        errordescription);
            }
        }
    }

    /**
     * Update a resource of the provided resource ID
     *
     * @param resourceRegistration details of the updated resource
     * @param resourceid           Resource ID of the resource
     * @throws UMAServiceException
     */
    public boolean updateResourceSet(String resourceid, ResourceRegistration resourceRegistration)
            throws SQLException, UMAServiceException {

        int resultSet = 0;

        String sql = SQLQueries.UPDATE_RESOURCE_META_DATA;

        try (Connection connection = JDBCPersistenceManager.getInstance().getDBConnection()) {

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(5, resourceid);
                preparedStatement.setString(1, resourceRegistration.getName());
                preparedStatement.setString(4, String.valueOf(resourceRegistration.getScopes()));
                for (MetaData metaData : resourceRegistration.getPropertyData()) {
                    preparedStatement.setString(6, metaData.getKey());
                    preparedStatement.setString(2, metaData.getKey());
                    preparedStatement.setString(3, metaData.getData());
                    resultSet += preparedStatement.executeUpdate();
                }
            }
            if (resultSet == 0) {
                connection.commit();
                return false;
            } else {
                connection.commit();
                return true;
            }
        } catch (SQLException e) {
            log.error("Error when retrieving the resource description. ");
            String errorMessage = "Error occured when updating resource description.";
            throw new UMAServiceException(ResourceConstants.ErrorMessages.ERROR_CODE_FAIL_TO_GET_RESOURCE,
                    errorMessage);
        }
    }
}
