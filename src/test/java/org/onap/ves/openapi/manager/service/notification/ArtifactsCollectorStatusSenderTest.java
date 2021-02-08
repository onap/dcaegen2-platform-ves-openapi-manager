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

package org.onap.ves.openapi.manager.service.notification;

import org.junit.jupiter.api.Test;
import org.onap.sdc.utils.DistributionStatusEnum;
import org.onap.ves.openapi.manager.model.DistributionStatusMessage;
import org.onap.ves.openapi.manager.service.testModel.ArtifactInfo;
import org.onap.ves.openapi.manager.service.validation.SchemaReferenceValidator;
import org.onap.sdc.api.consumer.IConfiguration;
import org.onap.sdc.api.notification.IArtifactInfo;
import org.onap.sdc.api.results.IDistributionClientResult;
import org.onap.sdc.impl.DistributionClientImpl;
import org.onap.sdc.impl.DistributionClientResultImpl;
import org.onap.sdc.utils.DistributionActionResultEnum;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArtifactsCollectorStatusSenderTest {

    private final IConfiguration configuration = mock(IConfiguration.class);
    private final DistributionClientImpl distributionClient = mock(DistributionClientImpl.class);
    private final ArtifactsCollectorStatusSender sender = new ArtifactsCollectorStatusSender(distributionClient);

    @Test
    void shouldSuccessfullySendStatusToEachDownloadedArtifacts() {
        //given
        DistributionClientResultImpl expectedResponse = new DistributionClientResultImpl(
                DistributionActionResultEnum.SUCCESS, "sample-response-message");
        List<DistributionClientResultImpl> expectedResponses = List.of(expectedResponse, expectedResponse);

        ArtifactInfo artifact = new ArtifactInfo(SchemaReferenceValidator.VES_EVENTS_ARTIFACT_TYPE);
        List<IArtifactInfo> artifacts = List.of(artifact, artifact);
        String consumerId = "ves-consumer";

        when(distributionClient.sendDownloadStatus(any())).thenReturn(expectedResponse);
        when(distributionClient.getConfiguration()).thenReturn(configuration);
        when(configuration.getConsumerID()).thenReturn(consumerId);

        //when
        List<IDistributionClientResult> responses = sender.sendDownloadOk("distribution-id", artifacts);

        //then
        assertThat(responses).isEqualTo(expectedResponses);
    }

    @Test
    void shouldSendDownloadErrorStatusToEachDownloadedArtifacts() {
        //given
        DistributionClientResultImpl expectedResponse = new DistributionClientResultImpl(
                DistributionActionResultEnum.GENERAL_ERROR, "sample-response-message");
        List<DistributionClientResultImpl> expectedResponses = List.of(expectedResponse, expectedResponse);

        ArtifactInfo artifact = new ArtifactInfo(SchemaReferenceValidator.VES_EVENTS_ARTIFACT_TYPE);
        List<IArtifactInfo> artifacts = List.of(artifact, artifact);
        String consumerId = "ves-consumer";
        when(distributionClient.sendDownloadStatus(any(),any())).thenReturn(expectedResponse);
        when(distributionClient.getConfiguration()).thenReturn(configuration);
        when(configuration.getConsumerID()).thenReturn(consumerId);
        long timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();

        DistributionStatusMessage dm = new DistributionStatusMessage(
                artifact.getArtifactURL(),
                "distribution-id",
                distributionClient.getConfiguration().getConsumerID(),
                timestamp,
                DistributionStatusEnum.DOWNLOAD_OK);

        //when
        List<IDistributionClientResult> responses = sender.sendDownloadError("distribution-id", artifacts);

        //then
        assertThat(responses).isEqualTo(expectedResponses);
        assertThat(dm.getArtifactURL()).isEqualTo(artifact.getArtifactURL());
        assertThat(dm.getDistributionID()).isEqualTo("distribution-id");
        assertThat(dm.getTimestamp()).isEqualTo(timestamp);
        assertThat(dm.getStatus()).isEqualTo(DistributionStatusEnum.DOWNLOAD_OK);
    }

    @Test
    void shouldNotSendAnyStatusWhenNoArtifactsAreDownloaded() {
        //given
        List<DistributionClientResultImpl> expectedResponses = List.of();
        List<IArtifactInfo> artifacts = List.of();

        //when
        List<IDistributionClientResult> responses = sender.sendDownloadOk("distribution-id", artifacts);

        //then
        assertThat(responses).isEqualTo(expectedResponses);
    }

}