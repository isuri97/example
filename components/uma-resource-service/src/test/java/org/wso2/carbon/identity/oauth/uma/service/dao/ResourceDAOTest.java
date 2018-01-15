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

import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.core.persistence.JDBCPersistenceManager;
import org.wso2.carbon.identity.oauth.uma.service.dao.util.DAOUtils;
import org.wso2.carbon.identity.oauth.uma.service.model.ResourceRegistration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.testng.Assert.assertFalse;


@PrepareForTest(JDBCPersistenceManager.class)

public class ResourceDAOTest extends DAOUtils {

    @Mock
    JDBCPersistenceManager jdbcPersistenceManager;

    private static final int SAMPLE_TENANT_ID = 1;

    private static final String DB_NAME = "regdb";

    String resourceId = "ca19a540f544777860e44e75f605d927";
    String resourceOwnerId = "ca19a540f544777860e44e75f605d927";

    @ObjectFactory
    public IObjectFactory getObjectFactory() {

        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }

    @BeforeClass
    public void setUp() throws Exception {

        initMocks(this);
        initiateH2Base(DB_NAME, getFilePath("resource.sql"));
        Timestamp timestamp = new Timestamp(890000);
        createResourceTable(DB_NAME, "76666hgf", "photo_album", timestamp, "ytr466", 1);
    }

    @DataProvider(name = "pkceEnabledDataProvider")
    public Object[][] provideData() throws Exception {

        return new Object[][]{
                {true},
                {false}
        };
    }

    @Test(expectedExceptions = ResourceServerException.class)
    public void testRegisterResourceSet() throws ResourceException, SQLException {

        try (Connection connection = getConnection(DB_NAME)) {
            mockStatic(JDBCPersistenceManager.class);
            when(JDBCPersistenceManager.getInstance()).thenReturn(jdbcPersistenceManager);
            when(jdbcPersistenceManager.getDBConnection()).thenReturn(connection);
            ResourceDAO resourceDAO = new ResourceDAO();
            ResourceRegistration resourceRegistration = new ResourceRegistration();
            resourceDAO.registerResourceSet(resourceRegistration);

        }
    }

    @Test
    public void testRetrieveResourceset() throws Exception {

        try (Connection connection = getConnection(DB_NAME)) {
            mockStatic(JDBCPersistenceManager.class);
            when(JDBCPersistenceManager.getInstance()).thenReturn(jdbcPersistenceManager);
            when(jdbcPersistenceManager.getDBConnection()).thenReturn(connection);
            ResourceDAO resourceDAO = new ResourceDAO();

            resourceDAO.retrieveResourceset(resourceId);
        }
    }

    @Test//(expectedExceptions = ResourceServerException.class)
    public void testRetrieveResourceIDs() throws Exception {

        try (Connection connection = getConnection(DB_NAME)) {
            mockStatic(JDBCPersistenceManager.class);
            when(JDBCPersistenceManager.getInstance()).thenReturn(jdbcPersistenceManager);
            when(jdbcPersistenceManager.getDBConnection()).thenReturn(connection);
            ResourceDAO resourceDAO = new ResourceDAO();

            resourceDAO.retrieveResourceIDs(resourceOwnerId);
        }

    }

    @Test(expectedExceptions = ResourceServerException.class)
    public void testUpdateResourceSet() throws Exception {

        try (Connection connection = getConnection(DB_NAME)) {
            mockStatic(JDBCPersistenceManager.class);
            when(JDBCPersistenceManager.getInstance()).thenReturn(jdbcPersistenceManager);
            when(jdbcPersistenceManager.getDBConnection()).thenReturn(connection);
            ResourceDAO resourceDAO = new ResourceDAO();
            ResourceRegistration resourceRegistration = new ResourceRegistration();
            resourceDAO.updateResourceSet(resourceId, resourceRegistration);

        }
    }

    @Test(expectedExceptions = ResourceServerException.class)
    public void testDeleteResourceSet() throws Exception {

        final String deleteSql = "SELECT * FROM IDN_RESOURCE WHERE RESOURCE_ID = ?";


        try (Connection connection = getConnection(DB_NAME)) {
            mockStatic(JDBCPersistenceManager.class);
            when(JDBCPersistenceManager.getInstance()).thenReturn(jdbcPersistenceManager);
            when(jdbcPersistenceManager.getDBConnection()).thenReturn(connection);
            ResourceDAO resourceDAO = new ResourceDAO();
            resourceDAO.deleteResourceSet(resourceId);

            PreparedStatement statement = connection.prepareStatement(deleteSql);
            statement.setString(1, resourceId);
            try (ResultSet resultSet = statement.executeQuery()) {
                assertFalse(resultSet.next());
            }
        }


    }

}

