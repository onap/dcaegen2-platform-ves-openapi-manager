/*
 * ============LICENSE_START=======================================================
 * VES-OPENAPI-MANAGER
 * ================================================================================
 * Copyright (C) 2021 Nokia. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.ves.openapi.manager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SchemaMap - model class with list of mappings of publicly stored OpenApi schemas to schemas stored locally in VES.
 */
@AllArgsConstructor
@Getter
public class SchemaMap {
    private final List<Mapping> mappings;

    /**
     * Single schemas mapping,
     * publicURL contains URL to externally located schema,
     * localURL contains URL located in VES container
     */
    @NoArgsConstructor
    @Getter
    public static class Mapping {
        private String publicURL;
        private String localURL;
    }
}
