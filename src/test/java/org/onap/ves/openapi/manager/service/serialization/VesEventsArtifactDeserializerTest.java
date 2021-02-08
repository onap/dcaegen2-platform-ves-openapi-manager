/*
 * ============LICENSE_START=======================================================
 * VES-OPENAPI-MANAGER
 * ================================================================================
 * Copyright (C) 2020 Nokia. All rights reserved.
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
import org.junit.jupiter.api.Test;
import org.onap.ves.openapi.manager.service.validation.SchemaReferenceValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

class VesEventsArtifactDeserializerTest {

    private static final String EVENT_DOMAIN_PATH = "/event/structure/commonEventHeader/structure/domain/value";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final VesEventsArtifactDeserializer deserializer = new VesEventsArtifactDeserializer(objectMapper);


    @Test
    void shouldReturnObjectNodesPerEachYamlDocumentWhenByteCodeIsCorrect() throws IOException {
        //given
        byte[] bytecode = Files.readAllBytes(Paths.get("src/test/resources/ves_artifact_stndDefined_events.yaml"));
        int expectedDocuments = 3;

        //when
        List<ObjectNode> objectNodes = deserializer.deserializeMultiDocumentYaml(bytecode);

        //then
        assertThat(objectNodes.size()).isEqualTo(expectedDocuments);
        assertThat(objectNodes).anyMatch(isStndDefinedEvent());
    }

    @Test
    void shouldReturnEmptyListWhenInvalidYamlByteCodeIsGiven() {
        //given
        byte[] invalidBytecode = new byte[] {'i','n','v','a','l','i', 'd'};
        //when
        List<ObjectNode> objectNodes = deserializer.deserializeMultiDocumentYaml(invalidBytecode);

        //then
        assertThat(objectNodes.size()).isZero();
    }

    private Predicate<ObjectNode> isStndDefinedEvent() {
        return event -> event.at(EVENT_DOMAIN_PATH).asText()
                .equals(SchemaReferenceValidator.STND_DEFINED_DOMAIN);
    }

}