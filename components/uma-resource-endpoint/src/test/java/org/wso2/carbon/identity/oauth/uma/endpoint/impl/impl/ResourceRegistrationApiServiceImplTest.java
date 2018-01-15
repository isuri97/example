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

import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.ErrorDTO;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.ResourceDetailsDTO;
import org.wso2.carbon.identity.oauth.uma.endpoint.impl.exceptions.ResourceEndpointException;
import org.wso2.carbon.identity.oauth.uma.endpoint.impl.util.ResourceUtils;
import org.wso2.carbon.identity.oauth.uma.service.ResourceConstants;
import org.wso2.carbon.identity.oauth.uma.service.model.ResourceRegistration;
import org.wso2.carbon.identity.oauth.uma.service.service.ResourceServiceImpl;


import javax.ws.rs.core.Response;



import static org.mockito.Matchers.any;

import static org.mockito.Mockito.reset;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

@PowerMockIgnore("javax.*")
@PrepareForTest({ResourceUtils.class})
public class ResourceRegistrationApiServiceImplTest {

    private ResourceRegistrationApiServiceImpl resourcesApiService = new ResourceRegistrationApiServiceImpl();
    private String someResourceId;
    private String someResourceName;
    @Mock
    private ResourceServiceImpl resourceService;

    @ObjectFactory
    public IObjectFactory getObjectFactory() {

        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }

    @BeforeMethod
    public void setUp() throws Exception {

        String someResourceId = " ";
        String someResourceName = "Resource1";
        mockStatic(ResourceUtils.class);
        when(ResourceUtils.getResourceService()).thenReturn(resourceService);
    }

    @DataProvider(name = "BuildRegisterResource")
    public Object[][] buildRegisterResource() {

        ResourceServiceClientException resourceClientException = new ResourceServiceClientException
                ("Resource Client Exception");
        ResourceException resourceException = new ResourceException
                ("Resource Exception");
        return new Object[][]{
                {Response.Status.OK, null},
                {Response.Status.BAD_REQUEST, resourceClientException},
                {Response.Status.CONFLICT, resourceClientException},
                {Response.Status.INTERNAL_SERVER_ERROR, resourceException}
        };
    }

    @Test(dataProvider = "BuildRegisterResource")
    public void testRegisterResource(Response.Status expectation, Throwable throwable) throws Exception {

        ResourceDetailsDTO resourceDetailsDTO = new ResourceDetailsDTO();
        resourceDetailsDTO.setName("ResourceName1");
        resourceDetailsDTO.setType("images");
        resourceDetailsDTO.setDescription("Some description");
        resourceDetailsDTO.setIcon_uri("http://www.example.com/icons/flower.png");

        if (Response.Status.CREATED.equals(expectation)) {
            when(resourceService.registerResourceSet(any(ResourceRegistration.class)))
                    .thenReturn(any(ResourceRegistration.class));
            assertEquals(resourcesApiService.registerResource(resourceDetailsDTO).getStatus(), Response.Status
                    .CREATED.getStatusCode(), "Error occurred while registering resources");
        } else if (Response.Status.BAD_REQUEST.equals(expectation)) {
            when(resourceService.registerResourceSet(any(ResourceRegistration.class))).thenThrow(throwable);
            //callRealMethod();
            try {
                resourcesApiService.registerResource(resourceDetailsDTO);
            } catch (ResourceEndpointException e) {
                assertEquals(e.getResponse().getStatus(), Response.Status.BAD_REQUEST.getStatusCode(),
                        "Cannot find HTTP Response, Bad Request in Case of " +
                                "ResourceServiceClientException");
                assertEquals(((ErrorDTO) (e.getResponse().getEntity())).getMessage(),
                        Response.Status.BAD_REQUEST.getReasonPhrase(), "Cannot find appropriate error message " +
                                "for HTTP Response, Bad Request");
            } finally {
                reset(resourceService);
            }

        } else if (Response.Status.CONFLICT.equals(expectation)) {
            ((ResourceServiceClientException) throwable).setErrorCode(ResourceConstants.ErrorMessages.
                    ERROR_CODE_CONFLICT_REQUEST_EXISTING_RESOURCE.getCode());
            when(resourceService.registerResourceSet(any(ResourceRegistration.class))).thenThrow(throwable);

            try {
                resourcesApiService.registerResource(resourceDetailsDTO);
            } catch (ResourceEndpointException e) {
                assertEquals(e.getResponse().getStatus(), Response.Status.CONFLICT.getStatusCode(),
                        "Cannot find HTTP Response, Conflict in Case of " +
                                "ResourceServiceClientException");
                assertEquals(((ErrorDTO) (e.getResponse().getEntity())).getMessage(),
                        Response.Status.CONFLICT.getReasonPhrase(), "Cannot find appropriate error message " +
                                "for HTTP Response, Conflict");
            } finally {
                reset(resourceService);
            }

        } else if (Response.Status.INTERNAL_SERVER_ERROR.equals(expectation)) {
            when(resourceService.registerResourceSet(any(ResourceRegistration.class))).
                    thenThrow(ResourceException.class);
            try {
                resourcesApiService.registerResource(resourceDetailsDTO);
            } catch (ResourceEndpointException e) {
                assertEquals(e.getResponse().getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                        "Cannot find HTTP Response, Internal Server Error in case of " +
                                "ResourceException");
                assertNull(e.getResponse().getEntity(), "Do not include error message in case of " +
                        "Server Exception");
            } finally {
                reset(resourceService);

            }
        }
    }

    /*@DataProvider(name = "BuildGetResource")
    public Object[][] buildGetResource() {

        ResourceServiceClientException resourceClientException = new ResourceServiceClientException
                ("Resource Client Exception");
        ResourceException resourceException = new ResourceException
                ("Resource Exception");
        return new Object[][]{
                {Response.Status.OK, null},
                {Response.Status.BAD_REQUEST, resourceClientException},
                {Response.Status.NOT_FOUND, resourceClientException},
                {Response.Status.INTERNAL_SERVER_ERROR, resourceException}
        };
    }

    @Test(dataProvider = "BuildGetResource")
    public void testGetResource(Response.Status expectation, Throwable throwable) throws
            Exception {


        if (Response.Status.OK.equals(expectation)) {
            when(resourceService.getResourceSetById(someResourceId)).thenReturn(any(ResourceRegistration.class));
            assertEquals(resourcesApiService.getResource(someResourceId).getStatus(), Response.Status.OK
                    .getStatusCode(), "Error occurred while getting a Resource");
        } else if (Response.Status.BAD_REQUEST.equals(expectation)) {
            when(resourceService.getResourceSetById(someResourceId)).thenThrow(throwable);
            try {
                resourcesApiService.getResource(someResourceId);
            } catch (ResourceEndpointException e) {
                assertEquals(e.getResponse().getStatus(), Response.Status.BAD_REQUEST.getStatusCode(),
                        "Cannot find HTTP Response, Bad Request in Case of " +
                                "ResourceServiceClientException");
                assertEquals(((ErrorDTO) (e.getResponse().getEntity())).getMessage(),
                        Response.Status.BAD_REQUEST.getReasonPhrase(), "Cannot find appropriate error message " +
                                "for HTTP Response, Bad Request");
            } finally {
                reset(resourceService);
            }
        } else if (Response.Status.NOT_FOUND.equals(expectation)) {
            ((ResourceServiceClientException) throwable).setErrorCode(ResourceConstants.ErrorMessages.
                    ERROR_CODE_NOT_FOUND_RESOURCE.getCode());
            when(resourceService.getResourceSetById(someResourceId)).thenThrow(throwable);

            try {
                resourcesApiService.getResource(someResourceId);
            } catch (ResourceEndpointException e) {
                assertEquals(e.getResponse().getStatus(), Response.Status.NOT_FOUND.getStatusCode(),
                        "Cannot find HTTP Response, Not Found in Case of " +
                                "ResourceServiceClientException");
                assertEquals(((ErrorDTO) (e.getResponse().getEntity())).getMessage(),
                        Response.Status.NOT_FOUND.getReasonPhrase(), "Cannot find appropriate error message " +
                                "for HTTP Response, Not Found");
            } finally {
                reset(resourceService);
            }
        } else if (Response.Status.INTERNAL_SERVER_ERROR.equals(expectation)) {
            when(resourceService.getResourceSetById(someResourceId)).thenThrow(ResourceException.class);
            try {
                resourcesApiService.getResource(someResourceId);
            } catch (ResourceEndpointException e) {
                assertEquals(e.getResponse().getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                        "Cannot find HTTP Response, Internal Server Error in case of " +
                                "resourceException");
                assertNull(e.getResponse().getEntity(), "Do not include error message in case of " +
                        "Server Exception");
            } finally {
                reset(resourceService);
            }
        }
    }*/

}
/*    @DataProvider(name = "BuildgetResources")
    public Object[][] getresourceIds() {

        return new Object[][]{
                {Response.Status.OK},
                {Response.Status.INTERNAL_SERVER_ERROR}
        };
    }

    @Test(dataProvider = "BuildgetResources")
    public void testGetResourceIds(Response.Status expectation) throws Exception {

        List<String> resourceList = null;
        String resourceOwnerID = "";

        if (Response.Status.OK.equals(expectation)) {
            when(resourceService.getResourceSetIds(any(String.class))).thenReturn(resourceList);
            Response response = resourcesApiService.getresourceIds(resourceOwnerID);
            assertEquals(response.getStatus(), Response.Status.OK.getStatusCode(),
                    "Error occurred while getting resources");

        } else if (Response.Status.INTERNAL_SERVER_ERROR.equals(expectation)) {
            when(resourceService.getResourceSetIds(any(String.class))).
                    thenThrow(ResourceException.class);

            try {
                resourcesApiService.getresourceIds(resourceOwnerID);
            } catch (ResourceEndpointException e) {
                assertEquals(e.getResponse().getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                        "Cannot find HTTP Response, Internal Server Error in case of " +
                                "ResourceException");
                assertNull(e.getResponse().getEntity(), "Do not include error message in case of " +
                        "Server Exception");
            }
        }
    }*/

   /* @DataProvider(name = "BuildUpdateResource")
    public Object[][] buildUpdateResource() {

        ResourceServiceClientException resourceClientException = new ResourceServiceClientException
                ("Resource Client Exception");
        ResourceException resourceException = new ResourceException
                ("Resource Exception");
        return new Object[][]{
                {Response.Status.OK, null},
                {Response.Status.BAD_REQUEST, resourceClientException},
                {Response.Status.NOT_FOUND, resourceClientException},
                {Response.Status.INTERNAL_SERVER_ERROR, resourceException}
        };
    }

    @Test(dataProvider = "BuildUpdateResource")
    public void testUpdateResource(Response.Status expectation, Throwable throwable) throws Exception {

        ResourceDetailsDTO resourceDetailsDTO = new ResourceDetailsDTO();
        if (Response.Status.CREATED.equals(expectation)) {
            when(resourceService.updateResourceSet(someResourceId, any(ResourceRegistration.class))).
                    thenReturn(any(ResourceRegistration.class));
            assertEquals(resourcesApiService.updateResource(someResourceId, resourceDetailsDTO).getStatus(),
                    Response.Status.CREATED.getStatusCode(),
                    "Error occurred while Updating resources");
        } else if (Response.Status.BAD_REQUEST.equals(expectation)) {
            when(resourceService.updateResourceSet(any(String.class), any(ResourceRegistration.class)))
                    .thenThrow(throwable);
            try {
                resourcesApiService.updateResource(someResourceId, resourceDetailsDTO);
            } catch (ResourceEndpointException e) {
                assertEquals(e.getResponse().getStatus(), Response.Status.BAD_REQUEST.getStatusCode(),
                        "Cannot find HTTP Response, Bad Request in Case of " +
                                "ResourceServiceClientException");
                assertEquals(((ErrorDTO) (e.getResponse().getEntity())).getMessage(),
                        Response.Status.BAD_REQUEST.getReasonPhrase(), "Cannot find appropriate error message " +
                                "for HTTP Response, Bad Request");
            } finally {
                reset(resourceService);
            }

        } else if (Response.Status.NOT_FOUND.equals(expectation)) {
           *//* ((ResourceServiceClientException) throwable).setErrorCode(ResourceConstants.ErrorMessages.
                    ERROR_CODE_CONFLICT_REQUEST_EXISTING_RESOURCE.getCode());*//*
            when(resourceService.updateResourceSet(any(String.class), any(ResourceRegistration.class))).
                    thenThrow(ResourceServiceClientException.class);

            try {
                resourcesApiService.updateResource(someResourceId, resourceDetailsDTO);
            } catch (ResourceEndpointException e) {
                assertEquals(e.getResponse().getStatus(), Response.Status.NOT_FOUND.getStatusCode(),
                        "Cannot find HTTP Response, Conflict in Case of " +
                                "ResourceServiceClientException");
                assertEquals(((ErrorDTO) (e.getResponse().getEntity())).getMessage(),
                        Response.Status.NOT_FOUND.getReasonPhrase(), "Cannot find appropriate error message " +
                                "for HTTP Response, Conflict");
            } finally {
                reset(resourceService);
            }


        } else if (Response.Status.INTERNAL_SERVER_ERROR.equals(expectation)) {
            when(resourceService.updateResourceSet(any(String.class), any(ResourceRegistration.class)))
                    .thenThrow(ResourceException.class);

            try {
                resourcesApiService.updateResource(someResourceId, resourceDetailsDTO);
            } catch (ResourceEndpointException e) {
                assertEquals(e.getResponse().getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                        "Cannot find HTTP Response, Internal Server Error in case of " +
                                "Resource Exception");
                assertNull(e.getResponse().getEntity(), "Do not include error message in case of " +
                        "Server Exception");
            } finally {
                reset(resourceService);

            }

        }
    }*/

  /*  @DataProvider(name = "BuildDeleteResource")
    public Object[][] buildDeleteResource() {

        ResourceServiceClientException resourceClientException = new ResourceServiceClientException
                ("Resource Client Exception");
        ResourceException resourceException = new ResourceException
                ("Resource Exception");
        return new Object[][]{
                {Response.Status.OK, null},
                {Response.Status.BAD_REQUEST, resourceClientException},
                {Response.Status.NOT_FOUND, resourceClientException}
        };
    }

    @Test(dataProvider = "BuildDeleteResource")
    public void testDeleteResource(Response.Status expectation, Throwable throwable) throws Exception {

        if (Response.Status.NO_CONTENT.equals(expectation)) {
            when(resourceService.deleteResourceSet(someResourceId)).thenReturn(true);
            assertEquals(resourcesApiService.deleteResource(someResourceId).getStatus(), Response.Status.CREATED.
                    getStatusCode(), "Error occurred while deleting resource");

        } else if (Response.Status.BAD_REQUEST.equals(expectation)) {
            when(resourceService.deleteResourceSet(any(String.class))).thenThrow(ResourceServiceClientException.class);

            try {
                resourcesApiService.deleteResource(someResourceId);
            } catch (ResourceEndpointException e) {
                assertEquals(e.getResponse().getStatus(), Response.Status.BAD_REQUEST.getStatusCode(),
                        "Cannot find HTTP Response, Bad Request in Case of " +
                                "IdentityOAuth2ScopeClientException");
                assertEquals(((ErrorDTO) (e.getResponse().getEntity())).getMessage(),
                        Response.Status.BAD_REQUEST.getReasonPhrase(), "Cannot find appropriate error message " +
                                "for HTTP Response, Bad Request");

            } finally {
                reset(resourceService);
            }
        } else if (Response.Status.NOT_FOUND.equals(expectation)) {
            when(resourceService.deleteResourceSet(any(String.class))).thenThrow(ResourceException.class);

            try {
                resourcesApiService.deleteResource(someResourceId);
            } catch (ResourceEndpointException e) {
                assertEquals(e.getResponse().getStatus(), Response.Status.NOT_FOUND.getStatusCode(),
                        "Cannot find HTTP Response, Not Found in Case of " +
                                "ResourceServiceClientException");
                assertEquals(((ErrorDTO) (e.getResponse().getEntity())).getMessage(),
                        Response.Status.NOT_FOUND.getReasonPhrase(), "Cannot find appropriate error message " +
                                "for HTTP Response, Not Found");
            } finally {
                reset(resourceService);
            }
        }*/



