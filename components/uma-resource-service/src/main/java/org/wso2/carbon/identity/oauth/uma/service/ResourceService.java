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

package org.wso2.carbon.identity.oauth.uma.service;

import org.wso2.carbon.identity.oauth.uma.service.exceptions.ResourceServerException;
import org.wso2.carbon.identity.oauth.uma.service.exceptions.UmaEndpointLayerException;
import org.wso2.carbon.identity.oauth.uma.service.exceptions.UmaException;
import org.wso2.carbon.identity.oauth.uma.service.exceptions.UmaServiceException;
import org.wso2.carbon.identity.oauth.uma.service.model.ResourceRegistration;

import java.sql.SQLException;
import java.util.List;

/**
 * This interface holds the implemented methods to ResourceServiceImpl.
 */
public interface ResourceService {

    public boolean deleteResourceSet(String resourceid) throws UmaException, SQLException;

    public List<String> getResourceSetIds(String resourceOwnerId) throws UmaException;

    public ResourceRegistration registerResourceSet(ResourceRegistration resourceRegistration)
            throws UmaException, ResourceServerException;

    public ResourceRegistration getResourceSetById(String resourceid)
            throws UmaServiceException, UmaEndpointLayerException;

    public ResourceRegistration updateResourceSet(String resourceid, ResourceRegistration resourceRegistration)
            throws SQLException, UmaException;
}
