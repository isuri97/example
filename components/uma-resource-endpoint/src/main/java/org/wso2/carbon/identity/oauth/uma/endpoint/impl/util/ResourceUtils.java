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

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.CreateResourceDTO;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.ListReadResourceDTO;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.ReadResourceDTO;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.ResourceDetailsDTO;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.UpdateResourceDTO;
import org.wso2.carbon.identity.oauth.uma.service.ResourceService;
import org.wso2.carbon.identity.oauth.uma.service.model.ResourceRegistation;
import org.wso2.carbon.identity.oauth.uma.service.model.ScopeDataDO;


/**
 * This class holds the util methods used by ResourceRegistrationApiServiceImpl.
 */
public class ResourceUtils {

    public static ResourceService getResourceService() {

        return (ResourceService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(ResourceService.class, null);
    }
    /**
     * Returns a resourceRegistration object
     *
     * @param resourceDetailsDTO specifies the details carried out by the ResourceDetailsDTO
     * @return A generic resourceregistration with the specified details
     */
    public static ResourceRegistation getResource(ResourceDetailsDTO resourceDetailsDTO) {

        ResourceRegistation resourceRegistration = new ResourceRegistation();
        resourceRegistration.setName(resourceDetailsDTO.getName());
        resourceRegistration.setScopes(resourceDetailsDTO.getResource_scopes());
        for (String scope:resourceRegistration.getScopes()){
            resourceRegistration.getScopeDataDOArr().add(new ScopeDataDO(resourceRegistration.getResourceId(),scope));
        }

        if (resourceDetailsDTO.getIcon_uri() != null) {
            resourceRegistration.getMetaData().put("icon_uri", resourceDetailsDTO.getIcon_uri());
        }
        if (resourceDetailsDTO.getType() != null) {
            resourceRegistration.getMetaData().put("type", resourceDetailsDTO.getType());
        }
        if (resourceDetailsDTO.getDescription() != null) {
            resourceRegistration.getMetaData().put("description", resourceDetailsDTO.getDescription());
        }
        return resourceRegistration;
    }

    /**
     * Returns a ReadResourceDTO object
     *
     * @param resourceRegistration specifies the details carried out by the ResourceRegistation Model class
     * @return A generic readresourceDTO with the specified details
     */
    public static ReadResourceDTO readResponse(ResourceRegistation resourceRegistration) {

        ReadResourceDTO readResourceDTO = new ReadResourceDTO();
        readResourceDTO.setResourceId(resourceRegistration.getResourceId());
        readResourceDTO.setName(resourceRegistration.getName());
        readResourceDTO.setType(resourceRegistration.getMetaData().get("type"));
        readResourceDTO.setDescription(resourceRegistration.getMetaData().get("description"));
        readResourceDTO.setIcon_uri(resourceRegistration.getMetaData().get("icon_uri"));
        readResourceDTO.setResource_scope(resourceRegistration.getScopes());
        return readResourceDTO;
    }

    /**
     * Returns a CreateResourceDTO object
     *
     * @param resourceRegistration specifies the details carried out by the ResourceRegistation Model class
     * @return A generic createResourceDTO with the specified details
     */
    public static CreateResourceDTO createResponse(ResourceRegistation resourceRegistration) {

        CreateResourceDTO createResourceDTO = new CreateResourceDTO();

        createResourceDTO.setResourceId(resourceRegistration.getResourceId());
        return createResourceDTO;
    }

    /**
     * Returns a UpdateResourceDTO object
     *
     * @param resourceRegistration specifies the details carried out by the ResourceRegistation Model class
     * @return A generic updateResourceDTO with the specified details
     */
    public static UpdateResourceDTO updateResponse(ResourceRegistation resourceRegistration) {

        UpdateResourceDTO updateResourceDTO = new UpdateResourceDTO();
        //updateResourceDTO.setResourceId(resourceRegistration.getResourceId());
        return updateResourceDTO;
    }

    public static ListReadResourceDTO listResourceId(ResourceRegistation resourceRegistration) {

        ListReadResourceDTO listReadResourceDTO = new ListReadResourceDTO();
        // listReadResourceDTO.setResourceId(resourceRegistration.getResourceId());
        return listReadResourceDTO;
    }
}
