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

package org.onap.ves.openapi.manager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.onap.sdc.api.notification.INotificationData;
import org.onap.sdc.http.SdcConnectorClient;
import org.onap.sdc.impl.DistributionClientDownloadResultImpl;
import org.onap.sdc.impl.DistributionClientImpl;
import org.onap.sdc.utils.DistributionActionResultEnum;
import org.onap.ves.openapi.manager.config.DistributionClientConfig;
import org.onap.ves.openapi.manager.config.ValidatorProperties;
import org.onap.ves.openapi.manager.service.notification.ArtifactsCollectorStatusSender;
import org.onap.ves.openapi.manager.service.notification.ValidationStatusSender;
import org.onap.ves.openapi.manager.service.serialization.SchemaMapDeserializer;
import org.onap.ves.openapi.manager.service.serialization.VesEventsArtifactDeserializer;
import org.onap.ves.openapi.manager.service.testModel.Service;
import org.onap.ves.openapi.manager.service.validation.ArtifactsValidator;
import org.onap.ves.openapi.manager.service.validation.SchemaReferenceValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServiceFunctionalTest {

    private final DistributionClientImpl distributionClient = mock(DistributionClientImpl.class);
    private final SdcConnectorClient sdcConnectorClient = mock(SdcConnectorClient.class);
    private final DistributionClientConfig config = mock(DistributionClientConfig.class);
    private final ValidatorProperties validatorProperties = mock(ValidatorProperties.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final VesEventsArtifactDeserializer vesEventsArtifactDeserializer = new VesEventsArtifactDeserializer(objectMapper);
    private final SchemaMapDeserializer schemaMapDeserializer = new SchemaMapDeserializer(objectMapper);

    private final List<ArtifactsValidator> validators = List.of(new SchemaReferenceValidator(vesEventsArtifactDeserializer, schemaMapDeserializer, validatorProperties));
    private final ArtifactsCollector artifactsCollector = new ArtifactsCollector(sdcConnectorClient);
    private final ArtifactsCollectorStatusSender artifactsCollectorStatusSender = new ArtifactsCollectorStatusSender(distributionClient);
    private final ValidationStatusSender validationStatusSender = new ValidationStatusSender(distributionClient);
    private final ClientCallback clientCallback = new ClientCallback(validators, artifactsCollector, artifactsCollectorStatusSender, validationStatusSender);

    private final INotificationData service = new Service();


    @Test
    void shouldNotThrowExceptionWhenCallingCallbackAndArtifactIsValid() throws IOException {
        //given
        byte[] payload = Files.readAllBytes(Paths.get("src/test/resources/ves_artifact_stndDefined_events.yaml"));
        DistributionClientDownloadResultImpl message = new DistributionClientDownloadResultImpl(
            DistributionActionResultEnum.SUCCESS, "message", "artifact-name", payload);
        DistributionClientDownloadResultImpl responseStatus = new DistributionClientDownloadResultImpl(DistributionActionResultEnum.SUCCESS, "OK");

        when(sdcConnectorClient.downloadArtifact(any())).thenReturn(message);
        when(distributionClient.sendDownloadStatus(any())).thenReturn(responseStatus);
        when(distributionClient.sendDeploymentStatus(any())).thenReturn(responseStatus);
        when(distributionClient.getConfiguration()).thenReturn(config);
        when(config.getConsumerID()).thenReturn("consumer-id");
        when(validatorProperties.getSchemaMapPath()).thenReturn("src/test/resources/test-schema-map.json");
        when(validatorProperties.getEventDomainPath()).thenReturn("/event/structure/commonEventHeader/structure/domain/value");
        when(validatorProperties.getEventSchemaReferencePath()).thenReturn("/event/structure/stndDefinedFields/structure/schemaReference/value");

        //when
        //then
        assertDoesNotThrow(() -> clientCallback.activateCallback(service));
    }
}
