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
import org.junit.jupiter.api.Test;
import org.onap.ves.openapi.manager.model.SchemaMap;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class SchemaMapDeserializerTest {

    final ObjectMapper objectMapper = new ObjectMapper();
    private final SchemaMapDeserializer deserializer =
            new SchemaMapDeserializer(objectMapper);

    @Test
    void shouldReturnSchemaMapFromFileWhenFileExists() {
        //given
        List<String> expectedPublicURLs = getExpectedPublicURLs();
        List<String> expectedLocalURLs = getExpectedLocalURLs();
        String schemaMapPath = "src/test/resources/test-schema-map.json";

        //when
        SchemaMap schemaMap = deserializer.getSchemaMapFromFile(schemaMapPath);

        //then
        assertThat(schemaMap.getMappings().size()).isEqualTo(3);
        assertThat(schemaMap.getMappings().stream().map(SchemaMap.Mapping::getPublicURL).collect(Collectors.toList()))
                .isEqualTo(expectedPublicURLs);
        assertThat(schemaMap.getMappings().stream().map(SchemaMap.Mapping::getLocalURL).collect(Collectors.toList()))
                .isEqualTo(expectedLocalURLs);
    }

    @Test
    void shouldReturnEmptySchemaMapWhenFileDoesNotExist() {
        //given
        String schemaMapPath = "src/test/resources/not-existing-schema-map.yaml";

        //when
        SchemaMap schemaMap = deserializer.getSchemaMapFromFile(schemaMapPath);

        //then
        assertThat(schemaMap.getMappings().size()).isZero();
    }

    private List<String> getExpectedPublicURLs() {
        return List.of(
                "https://forge.3gpp.org/rep/sa5/MnS/tree/SA88-Rel16/OpenAPI/PerMeasJobCtlMnS.yaml",
                "https://forge.3gpp.org/rep/sa5/MnS/tree/SA88-Rel16/OpenAPI/PerThresMonMnS.yaml",
                "https://forge.3gpp.org/rep/sa5/MnS/tree/SA88-Rel16/OpenAPI/PerfDataStreamingMnS.yaml"
        );
    }

    private List<String> getExpectedLocalURLs() {
        return List.of(
                "3gpp/rep/sa5/MnS/tree/SA88-Rel16/OpenAPI/PerMeasJobCtlMnS.yaml",
                "3gpp/rep/sa5/MnS/tree/SA88-Rel16/OpenAPI/PerThresMonMnS.yaml",
                "3gpp/rep/sa5/MnS/tree/SA88-Rel16/OpenAPI/PerfDataStreamingMnS.yaml"
        );
    }
}