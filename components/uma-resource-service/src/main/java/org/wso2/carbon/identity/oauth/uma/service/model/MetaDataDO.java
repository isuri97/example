package org.wso2.carbon.identity.oauth.uma.service.model;

/**
 * Created by isuri on 1/23/18.
 */
public class MetaDataDO {

    private  int id;
    private String resourceId;
    private String propertyKey;
    private String propertyValue;

    public MetaDataDO () {}

    public MetaDataDO (String resourceId,String propertyKey,String propertyValue){
        this.resourceId = resourceId;
        this.propertyKey = propertyKey;
        this.propertyValue = propertyValue;
    }

    public String getResourceId() {

        return resourceId;
    }

    public void setResourceId(String resourceId) {

        this.resourceId = resourceId;
    }

    public String getPropertyKey() {

        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {

        this.propertyKey = propertyKey;
    }

    public String getPropertyValue() {

        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {

        this.propertyValue = propertyValue;
    }
}
