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

package org.wso2.carbon.identity.oauth.uma.service.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Results holder for resource management Code validation query
 */
public class ResourceRegistation {

    private String resourceId;

    private String name;

    private String resourceOwnerId;

    private String tenentId;

    private Map<String, String> metaData = new HashMap<>();

    private List<String> resourcescopes = new ArrayList<>();

    private List<String> resourceid = new ArrayList<>();

    private Timestamp timestamp;

    private MetaDataDO[] metaDataDOArr;

    private ScopeDataDO[] scopeDataDoArr;

    private String resourceOwner;

    public String getResourceId() {

        return resourceId;
    }

    public void setResourceId(String resourceId) {

        this.resourceId = resourceId;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public List<String> getScopes() {

        return resourcescopes;
    }


    public void setScopes(List<String> scopes) {

        this.resourcescopes = scopes;
    }

    public Timestamp getTimecreated() {

        return timestamp;
    }

    public void setTimecreated(Timestamp timecreated) {

        this.timestamp = timecreated;
    }

    public String getResourceOwnerId() {

        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {

        this.resourceOwnerId = resourceOwnerId;
    }

    public String getTenentId() {

        return tenentId;
    }

    public void setTenentId(String tenentId) {

        this.tenentId = tenentId;
    }

    public List<String> getResourceid() {

        return resourceid;
    }

    public void setResourceid(List<String> resourceid) {

        this.resourceid = resourceid;
    }

    public Map<String, String> getMetaData() {

        return metaData;
    }

    public MetaDataDO[] getMetaDataDOArr() {

        return metaDataDOArr;
    }

    public void setMetaDataDOArr(MetaDataDO[] metaDataDOArr) {

        this.metaDataDOArr = metaDataDOArr;
    }

    public ScopeDataDO[] getScopeDataDOArr() {

        return scopeDataDoArr;
    }

    public void setScopeDataDOArr(ScopeDataDO[] scopeDataDoArr) {

        this.scopeDataDoArr = scopeDataDoArr;
    }

    public String getResourceOwner() {

        return resourceOwner;
    }

    public void setResourceOwner(String resourceOwner) {

        this.resourceOwner = resourceOwner;
    }

    public ResourceRegistation() {

        this.timestamp = new Timestamp(System.currentTimeMillis());
        ;
        this.resourceId = UUID.randomUUID().toString();
        this.resourceOwnerId = "123";
        this.resourceOwner = "IS";
        this.tenentId = "1";

    }

    public ResourceRegistation(String name) {

        this.resourcescopes = resourcescopes;
        this.name = name;
    }
}
