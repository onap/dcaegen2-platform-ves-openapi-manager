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

package org.onap.ves.openapi.manager.service.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.onap.ves.openapi.manager.model.SchemaMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Log4j2
@Service
public class SchemaMapDeserializer {

    private static final String COULD_NOT_READ_SCHEMA_MAP_MESSAGE = "Couldn't read schema map from path: ";
    private final ObjectMapper objectMapper;

    /**
     * Constructor of SchemaMapDeserializer
     * @param objectMapper ObjectMapper for YAML parsing
     */
    @Autowired
    public SchemaMapDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Deserializes file content to SchemaMap object
     * @param schemaMapPath Path to file containing JSON formatted SchemaMap
     * @return SchemaMap
     */
    public SchemaMap getSchemaMapFromFile(String schemaMapPath) {
        SchemaMap schemaMap;
        try {
            File file = new File(schemaMapPath);
            List<SchemaMap.Mapping> mappings = Arrays.asList(objectMapper.readValue(file, SchemaMap.Mapping[].class));
            schemaMap = new SchemaMap(mappings);
        } catch (IOException e) {
            schemaMap = new SchemaMap(Collections.emptyList());
            log.error(COULD_NOT_READ_SCHEMA_MAP_MESSAGE + schemaMapPath, e);
        }
        return schemaMap;
    }
}
