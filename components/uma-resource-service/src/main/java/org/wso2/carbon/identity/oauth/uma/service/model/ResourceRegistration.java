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
import java.util.List;
import java.util.UUID;
/**
 * Results holder for resource management Code validation query
 */
public class ResourceRegistration {

    private String resourceId;

    private String name;

    private String resourceOwnerId;

    private String tenentId;

    public MetaData type = new MetaData();

    public MetaData iconuri = new MetaData();

    private MetaData description = new MetaData();

    private List<String> resourcescopes = new ArrayList<>();

    private List<String> resourceid = new ArrayList<>();

    private List<MetaData> propertyData = new ArrayList<>();

    private Timestamp timestamp;

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

    public MetaData getType() {
        return type;
    }

    public void setType(String key, String data) {
        type.setKey(key);
        type.setData(data);
    }

    public MetaData getIconuri() {
        return iconuri;
    }

    public void setIcon_uri(String key, String data) {
        iconuri.setKey(key);
        iconuri.setData(data);
    }

    public MetaData getDescription() {
        return description;
    }

    public void setDescription(String key, String data) {
        description.setKey(key);
        description.setData(data);

    }

    public List<String> getScopes() {
        return resourcescopes;
    }


    public void setScopes(List<String> scopes) {
        this.resourcescopes = scopes;
    }

    public List<MetaData> getPropertyData() {
        return propertyData;
    }

    public void setPropertyData(List<MetaData> propertyData) {
        this.propertyData = propertyData;
    }

    public Timestamp getTimecreated() {
        return timestamp; }

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

    public ResourceRegistration() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        ;
        this.resourceId = UUID.randomUUID().toString();
        this.resourceOwnerId = "123";
        this.tenentId = "1";

    }

    public ResourceRegistration(String name, MetaData description, List<String> resourcescopes, MetaData iconuri,
                                MetaData type) {
        this.resourcescopes = resourcescopes;
        this.iconuri = iconuri;
        this.type = type;
        this.name = name;
        this.description = description;
    }
}
