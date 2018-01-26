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

import java.util.HashMap;
import java.util.Map;

/**
 * This class holds the constants used by ResourceServiceImpl.
 */
public class ResourceConstants {

    private String code;
    private Map<Integer, ErrorMessages> map;

    public ResourceConstants() {

        this.code = "invalid_resource_id";
        this.map = initializeMapping();
    }

    private Map<Integer, ErrorMessages> initializeMapping() {

        Map<Integer, ErrorMessages> map = new HashMap<>();
        map.put(404, ErrorMessages.ERROR_CODE_NOT_FOUND_RESOURCE_ID);
        map.put(400, ErrorMessages.ERROR_CODE_FAIL_TO_GET_RESOURCE);
        return map;
    }

    public String getCode() {

        return code;
    }

    public Map<Integer, ErrorMessages> getMap() {

        return map;
    }

    /**
     * Error codes and messages.
     */
    public enum ErrorMessages {

        ERROR_CODE_UNEXPECTED("51007", "Unexpected error"),
        ERROR_CODE_FAIL_TO_GET_RESOURCE("60001", "Error occurred while retrieving Resource."),
        ERROR_CODE_NOT_FOUND_RESOURCE_ID("60002", "Resource id is not found."),
        ERROR_UNEXPECTED("6005", "Unexpected error.");

        private final String code;
        private final String message;

        ErrorMessages(String code, String message) {

            this.code = code;
            this.message = message;
        }

        public String getCode() {

            return this.code;
        }

        public String getMessage() {

            return this.message;
        }

        @Override
        public String toString() {

            return code + " - " + message;
        }
    }
}
