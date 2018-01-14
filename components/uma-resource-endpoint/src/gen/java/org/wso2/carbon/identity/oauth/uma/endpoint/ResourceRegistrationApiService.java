package org.wso2.carbon.identity.oauth.uma.endpoint;

import org.wso2.carbon.identity.oauth.uma.endpoint.*;
import org.wso2.carbon.identity.oauth.uma.endpoint.dto.*;

import org.wso2.carbon.identity.oauth.uma.endpoint.dto.ResourceDetailsDTO;
import org.wso2.carbon.identity.oauth.uma.service.exceptions.UmaException;

import javax.ws.rs.core.Response;

public abstract class ResourceRegistrationApiService {
    public abstract Response deleteResource(String resourceId);
    public abstract Response getResource(String resourceId);
    public abstract Response getresourceIds(String resourceOwnerId);
    public abstract Response registerResource(ResourceDetailsDTO resource);
    public abstract Response updateResource(String resourceId,ResourceDetailsDTO updateresource);
}

