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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Log4j2
@Service
public class VesEventsArtifactDeserializer {

    private static final String COULD_NOT_READ_ARTIFACT_CONTENT_MESSAGE = "Couldn't read artifact content";
    private final ObjectMapper objectMapper;

    /**
     * Constructor of VesEventsArtifactDeserializer
     * @param objectMapper ObjectMapper for YAML parsing
     */
    @Autowired
    public VesEventsArtifactDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Deserializes MultiDocument YAML given as byte array
     * @param data MultiDocument YAML as byte array
     * @return List of ObjectNodes, each Document is separate ObjectNode
     */
    public List<ObjectNode> deserializeMultiDocumentYaml(byte[] data) {
        List<ObjectNode> events = Collections.emptyList();
        try {
            YAMLParser yamlParser = new YAMLFactory().createParser(data);
            events = objectMapper.readValues(yamlParser, ObjectNode.class).readAll();
        } catch (IOException e) {
            log.error(COULD_NOT_READ_ARTIFACT_CONTENT_MESSAGE, e);
        }
        return events;
    }
}
