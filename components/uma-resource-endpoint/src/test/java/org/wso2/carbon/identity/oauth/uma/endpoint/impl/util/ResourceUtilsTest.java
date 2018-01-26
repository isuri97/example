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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.IObjectFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.CreateResourceDTO;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.ErrorDTO;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.ReadResourceDTO;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.ResourceDetailsDTO;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.UpdateResourceDTO;
import org.wso2.carbon.identity.oauth.uma.endpoint.impl.exceptions.ResourceEndpointException;
import org.wso2.carbon.identity.oauth.uma.service.model.MetaData;
import org.wso2.carbon.identity.oauth.uma.service.model.ResourceRegistation;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;


public class ResourceUtilsTest {

    private static final Log log = LogFactory.getLog(ResourceUtilsTest.class);

    @ObjectFactory
    public IObjectFactory getObjectFactory() {

        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }

    @Test
    public void testGetResourceService() throws Exception {

    }

    @DataProvider(name = "BuildResourceEndpointException")
    public Object[][] buildResourceEndpointException() {

        Response.Status status = Response.Status.BAD_REQUEST;
        Throwable throwable1 = new ResourceEndpointException(status);
        Throwable throwable2 = new RuntimeException("BAD_REQUEST_INVALID_REDIRECT_URI");
        return new Object[][]{
                {status, throwable1, true},
                {status, throwable1, false},
                {status, throwable2, true},
                {status, throwable2, false},
                {status, throwable1, true}
        };
    }

    @Test(dataProvider = "BuildResourceEndpointException")
    public void testHandleErrorResponse(Response.Status status, Throwable throwable, boolean isServerException)
            throws Exception {

        String message = "Resource";
        // To check whether exception generated correctly.
 /*       try {
            ResourceUtils.handleErrorResponse(status, message, throwable, isServerException, log);
            Assert.fail();
        } catch (ResourceEndpointException e) {
            assertEquals(e.getResponse().getStatus(), status.getStatusCode());
        }*/
    }

    @Test(description = "Testing getErrorDTO")
    public void testGetErrorDTO() throws Exception {

        ErrorDTO errorDTOexpected = new ErrorDTO();
        errorDTOexpected.setCode("AJYRKLWB68NSB9");
       // errorDTOexpected.setMessage("Lifecycle exception occurred");
        errorDTOexpected.setDescription("Error occurred while changing lifecycle state");

    }
    @Test
    public void testGetResourceDTO() throws Exception {

        List scope = new ArrayList();
        scope.add("scopes1");
        MetaData description = new MetaData();
        MetaData iconUri = new MetaData();
        MetaData type = new MetaData();
        ResourceRegistation resource = new ResourceRegistation("resource1", description, scope, iconUri, type);

        ResourceDetailsDTO resourceDetailsDTO = ResourceUtils.getResourceDTO(resource);
        assertEquals(resourceDetailsDTO.getIcon_uri(), null, "Actual IconUri is not match for expected IconUri");
        assertEquals(resourceDetailsDTO.getType(), null, "Actual type is not match for expected type");
        assertEquals(resourceDetailsDTO.getDescription(), null,
                "Actual description is not match for expected description");
        assertEquals(resourceDetailsDTO.getName(), null, "Actual name is not match for expected name");
    }


    @Test
    public void testGetResource() throws Exception {

        ResourceDetailsDTO resourceDetailsDTO = new ResourceDetailsDTO();
        List scopes = new ArrayList();
        resourceDetailsDTO.setResource_scopes(scopes);
        resourceDetailsDTO.setName("resource1");
        ResourceRegistation resource = ResourceUtils.getResource(resourceDetailsDTO);
        assertEquals(resource.getName(), "resource1");
        assertEquals(resource.getScopes(), scopes);
    }

    @Test
    public void testReadResponse() throws Exception {

        List scope = new ArrayList();
        scope.add("scopes1");
        MetaData description = new MetaData();
        MetaData iconUri = new MetaData();
        MetaData type = new MetaData();
        ResourceRegistation resource = new ResourceRegistation("Resource1", description, scope, iconUri, type);
        ReadResourceDTO readResourceDTO = ResourceUtils.readResponse(resource);
        assertEquals(readResourceDTO.getResource_scope(), scope, "Actual scopes are not match for expected scopes");
        assertFalse(readResourceDTO.getResource_scope().get(0).contains("Scopes1"));
        assertEquals(readResourceDTO.getIcon_uri(), null, "Actual IconUri is not match for expected IconUri");
        assertEquals(readResourceDTO.getType(), null, "Actual type is not match for expected type");
        assertEquals(readResourceDTO.getDescription(), null, "Actual description is not match for expected " +
                "description");
        assertEquals(readResourceDTO.getName(), "Resource1", "Actual name is not match for expected name");
    }

    @Test
    public void testCreateResponse() throws Exception {

        List scope = new ArrayList();
        scope.add("scopes1");
        MetaData description = new MetaData();
        MetaData iconUri = new MetaData();
        MetaData type = new MetaData();

        ResourceRegistation resource = new ResourceRegistation("Resource1", description, scope, iconUri, type);
        CreateResourceDTO createResourceDTO = ResourceUtils.createResponse(resource);
        assertEquals(createResourceDTO.getResourceId(), null, "Actual ResourceId is not match for expected " +
                "ResourceId");
        assertEquals(createResourceDTO.getPolicy_uri(), null, "Actual Policy_uri is not match for expected " +
                "Policy_uri");
    }

    @Test
    public void testUpdateResponse() throws Exception {

        List scope = new ArrayList();
        scope.add("scopes1");
        MetaData description = new MetaData();
        MetaData iconUri = new MetaData();
        MetaData type = new MetaData();

        ResourceRegistation resource = new ResourceRegistation("Resource1", description, scope, iconUri, type);
        UpdateResourceDTO updateResourceDTO = ResourceUtils.updateResponse(resource);
        assertEquals(updateResourceDTO.getResourceId(), null, "Actual ResourceId is not match for expected " +
                "ResourceId");
    }
}
