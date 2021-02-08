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

package org.onap.ves.openapi.manager.service.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.onap.sdc.api.notification.IArtifactInfo;
import org.onap.ves.openapi.manager.config.ValidatorProperties;
import org.onap.ves.openapi.manager.model.Artifact;
import org.onap.ves.openapi.manager.model.ArtifactValidationResult;
import org.onap.ves.openapi.manager.model.SchemaMap;
import org.onap.ves.openapi.manager.service.serialization.SchemaMapDeserializer;
import org.onap.ves.openapi.manager.service.serialization.VesEventsArtifactDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Log4j2
@Service
public class SchemaReferenceValidator implements ArtifactsValidator {

    private static final String HASH_KEY = "#";
    private static final String NEW_LINE = "\n";
    static final String STND_DEFINED_DOMAIN = "stndDefined";
    static final String SCHEMA_REFERENCE_ERROR_MESSAGE = "Schema reference is not present in VES schema map.";

    private final VesEventsArtifactDeserializer vesEventsArtifactDeserializer;
    private final SchemaMapDeserializer schemaMapDeserializer;
    private final ValidatorProperties validatorProperties;

    /**
     * Constructor of SchemaReferenceValidator
     * @param vesEventsArtifactDeserializer Deserializer of VES_EVENT type artifact
     * @param schemaMapDeserializer Deserializer of Schema Map
     * @param validatorProperties Properties required by validator
     */
    @Autowired
    public SchemaReferenceValidator(VesEventsArtifactDeserializer vesEventsArtifactDeserializer,
                                    SchemaMapDeserializer schemaMapDeserializer,
                                    ValidatorProperties validatorProperties) {
        this.vesEventsArtifactDeserializer = vesEventsArtifactDeserializer;
        this.schemaMapDeserializer = schemaMapDeserializer;
        this.validatorProperties = validatorProperties;
    }

    /**
     * Validates given VES_EVENT type artifacts
     * @param artifacts List of artifacts: descriptions and contents
     * @return List of ArtifactValidationResult, validation results
     */
    @Override
    public List<ArtifactValidationResult> validate(List<Artifact> artifacts) {
        return artifacts.stream()
                .map(this::getArtifactValidationResult)
                .collect(Collectors.toList());
    }

    private ArtifactValidationResult getArtifactValidationResult(Artifact artifact) {
        List<JsonNode> stndDefinedEvents = getStndDefinedEvents(artifact.getContent());
        List<String> publicUrls = getPublicUrls();

        Set<String> invalidSchemaReferences = stndDefinedEvents.stream()
                .map(event -> getInvalidReferences(event, publicUrls))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        return getValidationResult(artifact.getDescription(), invalidSchemaReferences);
    }

    private ArtifactValidationResult getValidationResult(IArtifactInfo artifact, Set<String> invalidReferences) {
        boolean isValid = invalidReferences.isEmpty();
        String message = getMessage(invalidReferences);
        return new ArtifactValidationResult(artifact, isValid, message, this);
    }

    private String getMessage(Set<String> invalidSchemaReferences) {
        final String emptyMessage = "";
        return invalidSchemaReferences.isEmpty() ? emptyMessage : generateMessage(invalidSchemaReferences);
    }

    private String generateMessage(Set<String> invalidSchemaReferences) {
        return SCHEMA_REFERENCE_ERROR_MESSAGE + ":" + NEW_LINE + String.join(NEW_LINE, invalidSchemaReferences);
    }

    private List<JsonNode> getStndDefinedEvents(byte[] artifactData) {
        List<ObjectNode> events = vesEventsArtifactDeserializer.deserializeMultiDocumentYaml(artifactData);
        return events.stream()
                .filter(this::isStndDefinedEvent)
                .collect(Collectors.toList());
    }

    private boolean isStndDefinedEvent(ObjectNode event) {
        String actualDomain = event.at(validatorProperties.getEventDomainPath()).asText();
        return actualDomain.equals(STND_DEFINED_DOMAIN);
    }

    private List<String> getPublicUrls() {
        String schemaMapPath = validatorProperties.getSchemaMapPath();
        List<SchemaMap.Mapping> mappings = schemaMapDeserializer.getSchemaMapFromFile(schemaMapPath).getMappings();
        return mappings.stream()
                .map(SchemaMap.Mapping::getPublicURL)
                .collect(Collectors.toList());
    }

    private List<String> getInvalidReferences(JsonNode event, List<String> publicUrls) {
        String schemaReferencePath = validatorProperties.getEventSchemaReferencePath();
        JsonNode schemaReference = event.at(schemaReferencePath);
        Stream<JsonNode> schemaReferenceStream = Stream.empty();

        if (schemaReference.isArray()) {
            schemaReferenceStream = StreamSupport.stream(schemaReference.spliterator(), false);
        } else if (schemaReference.isValueNode()) {
            schemaReferenceStream = Stream.of(schemaReference);
        }

        return getInvalidSchemaReferences(schemaReferenceStream, publicUrls);
    }

    private List<String> getInvalidSchemaReferences(Stream<JsonNode> schemaReferencesStream, List<String> publicUrls) {
        return schemaReferencesStream
                .map(this::fetchUrlFromSchemaReference)
                .filter(reference -> !publicUrls.contains(reference))
                .collect(Collectors.toList());
    }

    private String fetchUrlFromSchemaReference(JsonNode schemaReference) {
        final int urlPartIndex = 0;
        return schemaReference.asText().split(HASH_KEY)[urlPartIndex];
    }
}
