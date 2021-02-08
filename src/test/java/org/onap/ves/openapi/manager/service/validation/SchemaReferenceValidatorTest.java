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

package org.onap.ves.openapi.manager.service.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.onap.ves.openapi.manager.config.ValidatorProperties;
import org.onap.ves.openapi.manager.model.Artifact;
import org.onap.ves.openapi.manager.model.ArtifactValidationResult;
import org.onap.ves.openapi.manager.service.testModel.ArtifactInfo;
import org.onap.ves.openapi.manager.service.serialization.SchemaMapDeserializer;
import org.onap.ves.openapi.manager.service.serialization.VesEventsArtifactDeserializer;
import org.onap.sdc.api.notification.IArtifactInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SchemaReferenceValidatorTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final VesEventsArtifactDeserializer vesEventsArtifactDeserializer =
            new VesEventsArtifactDeserializer(objectMapper);
    private final SchemaMapDeserializer schemaMapDeserializer = new SchemaMapDeserializer(objectMapper);
    private final String schemaMapPath = "src/test/resources/test-schema-map.json";
    private final String eventDomain = "/event/structure/commonEventHeader/structure/domain/value";
    private final String schemaReference = "/event/structure/stndDefinedFields/structure/schemaReference/value";
    private final ValidatorProperties validatorProperties = mock(ValidatorProperties.class);

    private final SchemaReferenceValidator validator = new SchemaReferenceValidator(vesEventsArtifactDeserializer,
            schemaMapDeserializer, validatorProperties);

    @Test
    void shouldReturnSuccessValidationResultWhenValidVesArtifactIsGiven() throws IOException {
        //given
        ArtifactInfo artifactInfo = new ArtifactInfo(SchemaReferenceValidator.VES_EVENTS_ARTIFACT_TYPE);
        List<Artifact> artifacts = getArtifacts(artifactInfo,
                "src/test/resources/ves_artifact_stndDefined_events.yaml");
        List<ArtifactValidationResult> expectedValidationResults =
                getExpectedResults(artifactInfo, true, Strings.EMPTY);

        when(validatorProperties.getSchemaMapPath()).thenReturn(schemaMapPath);
        when(validatorProperties.getEventDomainPath()).thenReturn(eventDomain);
        when(validatorProperties.getEventSchemaReferencePath()).thenReturn(schemaReference);

        //when
        List<ArtifactValidationResult> validationResults = validator.validate(artifacts);

        //then
        assertThat(validationResults).isEqualTo(expectedValidationResults);
    }

    @Test
    void shouldReturnFailedValidationResultWhenInvalidVesArtifactIsGiven() throws IOException {
        //given
        ArtifactInfo artifactInfo = new ArtifactInfo(SchemaReferenceValidator.VES_EVENTS_ARTIFACT_TYPE);
        List<Artifact> artifacts = getArtifacts(artifactInfo,
                "src/test/resources/ves_artifact_invalid_stndDefined_events.yaml");
        List<ArtifactValidationResult> expectedValidationResults = getExpectedResults(artifactInfo, false,
                SchemaReferenceValidator.SCHEMA_REFERENCE_ERROR_MESSAGE);

        when(validatorProperties.getSchemaMapPath()).thenReturn(schemaMapPath);
        when(validatorProperties.getEventDomainPath()).thenReturn(eventDomain);
        when(validatorProperties.getEventSchemaReferencePath()).thenReturn(schemaReference);

        //when
        List<ArtifactValidationResult> validationResults = validator.validate(artifacts);
        List<String> validationResultsMessages =
                validationResults.stream()
                        .map(result -> SchemaReferenceValidator.SCHEMA_REFERENCE_ERROR_MESSAGE)
                        .collect(Collectors.toList());

        //then
        assertThat(validationResults).usingElementComparatorIgnoringFields("message")
                .isEqualTo(expectedValidationResults);
        assertThat(validationResultsMessages)
                .isNotEmpty()
                .allMatch(message -> message.contains(SchemaReferenceValidator.SCHEMA_REFERENCE_ERROR_MESSAGE));
    }

    private List<Artifact> getArtifacts(IArtifactInfo artifactInfo, String artifactFilePath) throws IOException {
        Path path = Paths.get(artifactFilePath);
        byte[] artifactByteCode = Files.readAllBytes(path);
        Artifact artifact = new Artifact(artifactInfo, artifactByteCode);
        return List.of(artifact);
    }

    private List<ArtifactValidationResult> getExpectedResults(IArtifactInfo artifactInfo, boolean isValid, String message) {
        ArtifactValidationResult result = new ArtifactValidationResult(artifactInfo, isValid,
                message, validator);
        return List.of(result);
    }
}