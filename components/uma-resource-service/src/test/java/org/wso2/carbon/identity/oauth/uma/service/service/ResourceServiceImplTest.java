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

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.oauth.uma.service.dao.ResourceDAO;
import org.wso2.carbon.identity.oauth.uma.service.model.MetaData;
import org.wso2.carbon.identity.oauth.uma.service.model.ResourceRegistration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;


@PrepareForTest({IdentityUtil.class})
public class ResourceServiceImplTest {

    @Mock
    ResourceRegistration mockedresourceRegistration;

    @Mock
    ResourceDAO resourceDAO;

    @Mock
    List<String> mockedScopeSet;


    @InjectMocks
    private ResourceServiceImpl resourceServiceImpl;

    @BeforeMethod
    public void setUp() throws Exception {

        resourceServiceImpl = new ResourceServiceImpl();
    }

    @ObjectFactory
    public IObjectFactory getObjectFactory() {

        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }

    @Test
    public void testRegisterResourceSet() throws Exception {

        String name = "dummyResourceName";
        MetaData description = new MetaData();
        MetaData iconUri = new MetaData();
        MetaData type = new MetaData();
        List<String> scopes = new ArrayList<>(Arrays.asList("dummyScopes"));
        mockedresourceRegistration = new ResourceRegistration(name, description, scopes, type, iconUri);

        ResourceRegistration resourceRegistration = resourceServiceImpl.registerResourceSet(mockedresourceRegistration);
        assertEquals(resourceRegistration.getName(), "dummyResourceName", "Expected name did not received");
        assertNotEquals(resourceRegistration.getDescription(), null, "Expected description did not received");
        assertNotEquals(resourceRegistration.getType(), null, "Expected type did not received");
        assertNotEquals(resourceRegistration.getIconuri(), null, "Expected iconuri did not received");
    }

    @Test
    public void testGetResourceSetIds() throws Exception {

        when(resourceDAO.retrieveResourceIDs(any(String.class))).thenReturn(mockedScopeSet);

        assertNotNull(resourceServiceImpl.getResourceSetIds(null), "Expected a not null object");
    }

    @Test(expectedExceptions = IdentityException.class)
    public void testGetResourceSetIdsWithException() throws Exception {

        when(resourceDAO.retrieveResourceIDs(any(String.class))).thenThrow(ResourceServerException.class);

        assertNotNull(resourceServiceImpl.getResourceSetIds(null), "Expected a not null object");
    }
/*

    @Test
    public void testGetResourceSetById() throws Exception {

        String resourceId = "dummyResourceId";
        when(resourceDAO.retrieveResourceset(any(String.class))).thenReturn(mockedresourceRegistration);
        assertNotNull(resourceServiceImpl.getResourceSetById(resourceId), "Expected a not null object");
    }

    @Test(expectedExceptions = IdentityException.class)
    public void testGetResourceSetByIdWithException() throws Exception {

        String resourceId = "dummyResourceId";
        when(resourceDAO.retrieveResourceset(any(String.class))).thenThrow(ResourceServerException.class);
        assertNotNull(resourceServiceImpl.getResourceSetById(resourceId), "Expected a not null object");
    }
*/



    @Test(expectedExceptions = IdentityException.class)
    public void testUpdateResourceSetWithException() throws Exception {

        String resourceId = "dummyResourceId";
        when(resourceDAO.updateResourceSet(any(String.class), any(ResourceRegistration.class)))
                .thenThrow(ResourceServerException.class);

        assertNotNull(resourceServiceImpl.updateResourceSet(resourceId, mockedresourceRegistration),
                "Expected a not null object");

    }


    @Test(expectedExceptions = IdentityException.class)
    public void testDeleteResourceSetWithException() throws Exception {

        String resourceId = "dummyResourceId";
        when(resourceDAO.deleteResourceSet(any(String.class))).thenThrow(ResourceServerException.class);
        assertNotNull(resourceServiceImpl.deleteResourceSet(resourceId), "Expected a not null object");
    }
}
