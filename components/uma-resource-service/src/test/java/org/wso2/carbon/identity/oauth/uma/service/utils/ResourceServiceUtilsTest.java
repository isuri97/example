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

package org.wso2.carbon.identity.oauth.uma.service.utils;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.oauth.uma.service.ResourceConstants;

public class ResourceServiceUtilsTest {

    @DataProvider(name = "BuildServerException")
    public Object[][] buildServerException() {

        return new Object[][]{
                {ResourceConstants.ErrorMessages.ERROR_CODE_UNEXPECTED, ""},
                {ResourceConstants.ErrorMessages.ERROR_CODE_UNEXPECTED, "error from bad request"}
        };
    }

    @Test(dataProvider = "BuildServerException")
    public void testGenerateServerException(ResourceConstants.ErrorMessages error, String data) throws Exception {

        Throwable e = new Throwable();
        try {
            ResourceServiceUtils.generateServerException(error, data, e);
        } catch (ResourceServerException ex) {
            Assert.assertEquals(ex.getMessage(), data);
        }
    }

    @Test(dataProvider = "BuildServerException")
    public void testGenerateServerException1(ResourceConstants.ErrorMessages error, String data) throws Exception {

        try {
            ResourceServiceUtils.generateServerException(error, data);
        } catch (ResourceServerException e) {
            Assert.assertEquals(e.getMessage(), data);
        }

    }

    @Test(dataProvider = "BuildServerException")
    public void testGenerateClientException(ResourceConstants.ErrorMessages error, String data) throws Exception {

        Throwable e = new Throwable();
        try {
            ResourceServiceUtils.generateClientException(error, data, e);
        } catch (ResourceServiceClientException ex) {
            Assert.assertEquals(ex.getMessage(), data);
        }
    }

    @Test(dataProvider = "BuildServerException")
    public void testGenerateClientException1(ResourceConstants.ErrorMessages error, String data) throws Exception {

        try {
            ResourceServiceUtils.generateClientException(error, data);
        } catch (ResourceServiceClientException e) {
            Assert.assertEquals(e.getMessage(), data);
        }
    }
}
