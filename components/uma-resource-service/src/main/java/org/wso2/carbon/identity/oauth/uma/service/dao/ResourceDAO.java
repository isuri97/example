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

import org.apache.bcel.generic.IF_ACMPEQ;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jasper.tagplugins.jstl.If;
import org.wso2.carbon.identity.core.persistence.JDBCPersistenceManager;
import org.wso2.carbon.identity.core.util.IdentityDatabaseUtil;
import org.wso2.carbon.identity.oauth.uma.service.ResourceConstants;
import org.wso2.carbon.identity.oauth.uma.service.exceptions.UMAException;
import org.wso2.carbon.identity.oauth.uma.service.exceptions.UMAServiceException;
import org.wso2.carbon.identity.oauth.uma.service.model.ResourceRegistation;
import org.wso2.carbon.identity.oauth.uma.service.model.ScopeDataDO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Data Access Layer functionality for Resource management. This includes storing, updating, deleting
 * and retrieving resources.
 */
public class ResourceDAO {

    private static final Log log = LogFactory.getLog(ResourceDAO.class);

    /**
     * Add a resource
     *
     * @param resource details of the registered resource
     * @return resourceId of resgistered resource description
     * @throws UMAServiceException ResourceException
     */
    public ResourceRegistation registerResource(ResourceRegistation resource) throws UMAServiceException {

        /*String resourcesql = SQLQueries.INSERT_RESOURCE;
        String metadatasql = SQLQueries.INSERT_INTO_RESOURCE_META_DATA;
        String scopesql = SQLQueries.INSERT_INTO_RESOURCE_SCOPE1;*/

        // PreparedStatement preparedStatement = null;
        //PreparedStatement purposeIdPrepStat = null;
        //ResultSet resultSet = null;
        Connection connection = IdentityDatabaseUtil.getDBConnection();
        String query = "INSERT INTO IDN_RESOURCE(RESOURCE_ID,RESOURCE_NAME,TIME_CREATED," +
                "RESOUCE_OWNER_ID,TENANT_ID) VALUES (?,?,?,?,?)";
        // String purposeIdQuery = "SELECT RESOURCE_ID FROM IDN_RESOURCE WHERE ID=?";

        try {
            connection.setAutoCommit(false);
            Savepoint savepoint = connection.setSavepoint();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, resource.getResourceId());
            preparedStatement.setString(2, resource.getName());
            preparedStatement.setTimestamp(3, resource.getTimecreated());
            preparedStatement.setString(4, resource.getResourceOwnerId());
            preparedStatement.setString(5, resource.getTenentId());
            preparedStatement.execute();

            try (ResultSet resultSet1 = preparedStatement.getGeneratedKeys()) {

                long id = resultSet1.getLong(1);

                //purposeIdPrepStat = connection.prepareStatement(purposeIdQuery);
                //purposeIdPrepStat.setString(1, resource.getResourceId());
                //resultSet1 = purposeIdPrepStat.executeQuery();
                // resultSet.first();
                //mapPurposeWithPurposeCategories(connection, id, resource.getMetaDataDOArr());
                mapScopeTable(connection, id, resource.getScopeDataDOArr());
                connection.commit();
                // ResourceRegistation resourceDetails = retrieveResource(resultSet1.getString(1));
                //purposeDetails=getPurposeDetailsById(resultSet.getInt(1));
                log.info("Successfully added the purpose details to the database");
            } catch (SQLException e) {
                try {
                    connection.rollback(savepoint);
                } catch (SQLException e1) {
                    log.error("Rollback error. Could not rollback purpose adding. - " + e.getMessage());
                    throw new UMAServiceException("Rollback error. Could not rollback purpose adding. - " + e
                            .getMessage(), e);
                }
                log.error("Database error. Could not add purpose details. - " + e.getMessage(), e);
                throw new UMAServiceException("Database error. Could not add purpose details. - " + e.getMessage(),
                        e);
           /* } finally {
                // DBUtils.closeAllConnections(connection, preparedStatement, purposeIdPrepStat, resultSet);
            }*/
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resource;
    }

   /* private void mapPurposeWithPurposeCategories(Connection connection, long id, MetaDataDO[]
            metaDataValues) throws UMAServiceException {
                String query = "INSERT INTO IDN_RESOURCE_META_DATA(ID_RESOURCE,PROPERTY_KEY,PROPERTY_VALUE)" +
                        "VALUES ((SELECT ID FROM IDN_RESOURCE WHERE RESOURCE_ID = ?),?,?) ON DUPLICATE KEY UPDATE " +
                        "ID_RESOURCE=?;";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    *//*for (MetaDataDO metadata : metaDataValues) {*//*
                        preparedStatement.setLong(1, id);
                        for (Map.Entry<String, String> entry : resource.getMetaData().entrySet()) {
                            preparedStatement.setString(2, entry.getKey());
                            preparedStatement.setString(3, entry.getValue());
                            preparedStatement.execute();
                    }
                } catch (SQLException e) {
                    log.error("Database error. Could not map purpose to purpose category. - " + e.getMessage(), e);
                    throw new UMAServiceException("Database error. Could not map purpose to purpose category. - " + e
                            .getMessage(), e);
                } finally {
                   // DBUtils.closeAllConnections(preparedStatement);
                }
            }*/


    private void mapScopeTable(Connection connection, long id, ScopeDataDO[]
            ScopeData) throws UMAServiceException {

        String query = "INSERT INTO IDN_SCOPE(ID_RESOURCE,SCOPE_NAME) VALUES ((SELECT ID FROM IDN_RESOURCE WHERE " +
                "RESOURCE_ID = ?),?) ON DUPLICATE KEY UPDATE ID_RESOURCE=?;";
        try {
            for (ScopeDataDO scopeDataDO : ScopeData) {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, id);
                preparedStatement.setString(2, scopeDataDO.getScopeName());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            log.error("Database error. Could not map purpose to personally identifiable info category. - " + e
                    .getMessage(), e);
            throw new UMAServiceException("Database error. Could not map purpose to personally identifiable info " +
                    "category. - " + e.getMessage(), e);
        } finally {
            //DBUtils.closeAllConnections(preparedStatement);
        }
    }
/*
    public ResourceRegistation getResourceDetailsById(int id) throws DataAccessException{
        ResourceRegistation resourceModel = new ResourceRegistation();
        Connection connection = IdentityDatabaseUtil.getDBConnection();
        PreparedStatement selectPrepStat=null;
        ResultSet resultSet=null;

            String selectQuery="SELECT A.*,B.THIRD_PARTY_NAME FROM PURPOSES AS A,THIRD_PARTY AS B WHERE B" +
                    ".THIRD_PARTY_ID=A.THIRD_PARTY_ID AND PURPOSE_ID=?;";
            try {
                selectPrepStat=connection.prepareStatement(selectQuery);
                selectPrepStat.setInt(1,id);
                resultSet=selectPrepStat.executeQuery();
                resultSet.first();
                purpose.setPurposeId(resultSet.getInt(1));
                purpose.setPurpose(resultSet.getString(2));
                purpose.setPrimaryPurpose(resultSet.getString(3));
                purpose.setTermination(resultSet.getString(4));
                purpose.setThirdPartyDis(resultSet.getString(5));
                purpose.setThirdPartyId(resultSet.getInt(6));
                purpose.setthirdPartyName(resultSet.getString(7));

                PurposeCategoryDO[] purposeCategoryDOS= getPurposeCatsForPurposeConf(connection, id).toArray(new PurposeCategoryDO[0]);
                purpose.setPurposeCategoryDOArr(purposeCategoryDOS);

                PiiCategoryDO[] piiCategoryDOS= getPersonalIdentifyCatForPurposeConf(connection, id).toArray(new PiiCategoryDO[0]);
                purpose.setpiiCategoryArr(piiCategoryDOS);
            } catch (SQLException e) {
                log.error("Database error. Could not get details of the purpose. - "+e.getMessage(),e);
                throw new DataAccessException("Database error. Could not get details of the purpose. - "+e.getMessage
                        (),e);
            }
        }
        return purpose;
    }*/



           /* try (PreparedStatement preparedStatement = connection.prepareStatement(resourcesql)) {
                preparedStatement.setString(1, resource.getResourceId());
                preparedStatement.setString(2, resource.getName());
                preparedStatement.setTimestamp(3, resource.getTimecreated());
                preparedStatement.setString(4, resource.getResourceOwnerId());
                preparedStatement.setString(5, resource.getTenentId());
                preparedStatement.execute();
            }*/
   /*         try (PreparedStatement preparedStatement = connection.prepareStatement(metadatasql)) {
                preparedStatement.setString(1, resource.getResourceId());
                for (Map.Entry<String, String> entry : resource.getMetaData().entrySet()) {
                    preparedStatement.setString(2, entry.getKey());
                    preparedStatement.setString(3, entry.getValue());
                    preparedStatement.execute();
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(scopesql)) {
                preparedStatement.setString(1, resource.getResourceId());
                preparedStatement.setString(2, String.valueOf(resource.getScopes()));
                preparedStatement.execute();
                connection.commit();
            }

        } catch (SQLException e) {
            log.error("Error when retrieving the resource description. ");
            String errordescription = "Resource Id can not be found in data base";
            throw new UMAServiceException(ResourceConstants.ErrorMessages.ERROR_CODE_FAIL_TO_GET_RESOURCE,
                    errordescription);
        }
        return resource;
    }*/

    /**
     * Get a resource by resourceId
     *
     * @param resourceid Id of the resource
     * @return resource description for the provided ID
     * @throws UMAServiceException
     */
    public ResourceRegistation retrieveResource(String resourceid) throws UMAServiceException {

        String sql = SQLQueries.GET_ALL_RESOURCE_FROM_ID;
        ResourceRegistation resourceRegistration = new ResourceRegistation();

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
                                resourceRegistration.getMetaData().put(resultSet.getString("PROPERTY_KEY"),
                                        resultSet.getString("PROPERTY_VALUE"));

                            }
                            if (resultSet.getString("PROPERTY_KEY").equals("type")) {
                                resourceRegistration.getMetaData().put(resultSet.getString("PROPERTY_KEY"),
                                        resultSet.getString("PROPERTY_VALUE"));
                            }
                            if (resultSet.getString("PROPERTY_KEY").equals("description")) {
                                resourceRegistration.getMetaData().put(resultSet.getString("PROPERTY_KEY"),
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
     * @param resourceId Resource ID of the resource
     * @throws UMAServiceException
     */
    public boolean deleteResource(String resourceId) throws SQLException, UMAException {

        Connection connection = IdentityDatabaseUtil.getDBConnection();
        String deleteResource = "DELETE FROM IDN_RESOURCE WHERE ID_RESOURCE = ? WHERE ID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteResource);
            preparedStatement.setString(1, resourceId);
            int rowsAffected = preparedStatement.executeUpdate();
            connection.commit();
            return rowsAffected > 0;

            deleteMetaDataMapWithId(connection, resourceId);
            deleteScopeMapWithId(connection, resourceId);

        } catch (SQLException e) {
            log.error("Database error. Could not delete purpose category. - " + e.getMessage(), e);
            throw new UMAException("Database error. Could not delete purpose categories. - " + e.getMessage(), e)
        }
    }


    private void deleteMetaDataMapWithId(Connection connection, String resourceId) throws SQLException,
            UMAException {

        String deletemetadata = "DELETE FROM IDN_RESOURCE_META_DATA WHERE IDN_RESOURCE_META_DATA.ID_RESOURCE = ( " +
                "SELECT ID FROM IDN_RESOURCE WHERE RESOURCE_ID = ?";
        //delete from table2 where table2.ID_RESOURCE = (SELECT ID FROM table1 WHERE RESOURCE_ID = ?);


        try {
            PreparedStatement deletePurposeCatStat = connection.prepareStatement(deletemetadata);
            deletePurposeCatStat.setString(1, resourceId);
            deletePurposeCatStat.executeUpdate();
            deleteScopeMapWithId(connection, resourceId);
        } catch (SQLException e) {
            log.error("Database error. Could not delete purpose category. - " + e.getMessage(), e);
            throw new UMAException("Database error. Could not delete purpose categories. - " + e.getMessage(), e);
        }
    }


    private void deleteScopeMapWithId(Connection connection, String resourceId) throws SQLException,
            UMAException {

        PreparedStatement deletePersonalInfoCatStat = null;
        String deleteScopeInfo = "DELETE FROM IDN_SCOPE WHERE ID=?";
        try {
            deletePersonalInfoCatStat = connection.prepareStatement(deleteScopeInfo);
            deletePersonalInfoCatStat.setString(1, resourceId);
            deletePersonalInfoCatStat.executeUpdate();
        } catch (SQLException e) {
            log.error("Database error. Could not delete personally identifiable categories. - " + e.getMessage(), e);
            throw new UMAException("Database error. Could not delete personally identifiable categories. - " + e
                    .getMessage(), e);
        }
    }

        /*String sql = SQLQueries.DELETE_RESOURCE_BY_ID;

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
        }*/









    /**
     * Update a resource of the provided resource ID
     *
     * @param resourceRegistration details of the updated resource
     * @param resourceid           Resource ID of the resource
     * @throws UMAServiceException
     */
    public boolean updateResource(String resourceid, ResourceRegistation resourceRegistration)
            throws SQLException, UMAServiceException {

        int resultSet = 0;

        String sql = SQLQueries.UPDATE_RESOURCE_META_DATA;

        try (Connection connection = JDBCPersistenceManager.getInstance().getDBConnection()) {

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(5, resourceid);
                preparedStatement.setString(1, resourceRegistration.getName());
                preparedStatement.setString(4, String.valueOf(resourceRegistration.getScopes()));
                for (Map.Entry<String, String> entry : resourceRegistration.getMetaData().entrySet()) {
                    preparedStatement.setString(6, entry.getKey());
                    preparedStatement.setString(2, entry.getKey());
                    preparedStatement.setString(3, entry.getValue());
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
