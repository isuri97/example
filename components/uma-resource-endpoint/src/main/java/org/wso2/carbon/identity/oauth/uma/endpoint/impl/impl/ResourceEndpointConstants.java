package org.wso2.carbon.identity.oauth.uma.endpoint.impl.impl;

import org.wso2.carbon.identity.oauth.uma.service.ResourceConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by isuri on 1/23/18.
 */
public class ResourceEndpointConstants {

    private String code;
    private Map<Integer, ResourceConstants.ErrorMessages> map;

    public ResourceEndpointConstants (){
        this.code = "invalid_resource_id";
        this.map = initializeMapping();
    }
    private Map<Integer, ResourceConstants.ErrorMessages> initializeMapping() {

        Map<Integer, ResourceConstants.ErrorMessages> map = new HashMap<>();
        map.put(404, ResourceConstants.ErrorMessages.ERROR_CODE_NOT_FOUND_RESOURCE_ID);
        map.put(400, ResourceConstants.ErrorMessages.ERROR_CODE_FAIL_TO_GET_RESOURCE);
        return map;
    }

    public String getCode() {

        return code;
    }

    public Map<Integer, ResourceConstants.ErrorMessages> getMap() {

        return map;
    }

}
