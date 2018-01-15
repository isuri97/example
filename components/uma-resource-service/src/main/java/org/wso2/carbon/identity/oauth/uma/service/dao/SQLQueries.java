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

package org.wso2.carbon.identity.oauth.uma.service.dao;


/**
 * SQL queries related to dao file.
 */
public class SQLQueries {

    public static final String IDN_RESOURCE_TABLE = "IDN_RESOURCE";
    public static final String IDN_RESOURCE_META_DATA = "IDN_RESOURCE_META_DATA";
    public static final String IDN_RESOURCE_SCOPE = " IDN_RESOURCE_SCOPE";

    public static final String INSERT_RESOURCE = "INSERT INTO IDN_RESOURCE(RESOURCE_ID,RESOURCE_NAME,TIME_CREATED," +
            "RESOUCE_OWNER_ID,TENANT_ID) VALUES (?,?,?,?,?)";

    public static final String INSERT_INTO_RESOURCE_META_DATA = "INSERT INTO IDN_RESOURCE_META_DATA(RESOURCE_ID," +
            "PROPERTY_KEY,PROPERTY_VALUE) VALUES (?,?,?)";

    public static final String INSERT_INTO_RESOURCE_SCOPE1 = "INSERT INTO IDN_SCOPE (RESOURCE_ID,NAME)VALUES (?,?)";

    public static final String UPDATE_RESOURCE_META_DATA = "UPDATE IDN_RESOURCE\n" +
            "JOIN IDN_RESOURCE_META_DATA on IDN_RESOURCE.RESOURCE_ID = IDN_RESOURCE_META_DATA.RESOURCE_ID\n" +
            "JOIN IDN_SCOPE on IDN_RESOURCE_META_DATA.RESOURCE_ID = IDN_SCOPE.RESOURCE_ID \n" +
            "SET IDN_RESOURCE.RESOURCE_NAME = ?, IDN_RESOURCE_META_DATA.PROPERTY_KEY = ?,\n" +
            " IDN_RESOURCE_META_DATA.PROPERTY_VALUE = ?, IDN_SCOPE.NAME = ?\n" +
            " WHERE IDN_RESOURCE.RESOURCE_ID = ? AND IDN_RESOURCE_META_DATA.PROPERTY_KEY = ?";

    public static final String DELETE_RESOURCE_BY_ID = "DELETE IDN_SCOPE,IDN_RESOURCE_META_DATA,IDN_RESOURCE " +
            "FROM IDN_RESOURCE  \n" +
            "INNER JOIN IDN_RESOURCE_META_DATA on IDN_RESOURCE.RESOURCE_ID = IDN_RESOURCE_META_DATA.RESOURCE_ID\n" +
            "INNER JOIN IDN_SCOPE on IDN_RESOURCE_META_DATA.RESOURCE_ID = IDN_SCOPE.RESOURCE_ID\n" +
            "WHERE IDN_RESOURCE.RESOURCE_ID = ?";

    public static final String GET_RESOURCE_IDS = "SELECT RESOURCE_ID FROM IDN_RESOURCE WHERE RESOUCE_OWNER_ID = ?";

    public static final String GET_ALL_RESOURCE_FROM_ID = "SELECT IDN_RESOURCE.RESOURCE_ID,IDN_RESOURCE" +
            ".RESOURCE_NAME," +
            "IDN_RESOURCE_META_DATA.PROPERTY_KEY,IDN_RESOURCE_META_DATA.PROPERTY_VALUE,IDN_SCOPE.NAME\n" +
            "FROM (IDN_RESOURCE\n" +
            "INNER JOIN IDN_RESOURCE_META_DATA on IDN_RESOURCE.RESOURCE_ID = IDN_RESOURCE_META_DATA.RESOURCE_ID)\n" +
            "INNER JOIN IDN_SCOPE on IDN_RESOURCE_META_DATA.RESOURCE_ID = IDN_SCOPE.RESOURCE_ID\n" +
            "WHERE IDN_RESOURCE.RESOURCE_ID = ?";
}
