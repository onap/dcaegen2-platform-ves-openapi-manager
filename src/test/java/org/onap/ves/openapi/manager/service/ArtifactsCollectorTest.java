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

import org.junit.jupiter.api.Test;
import org.onap.sdc.api.notification.IArtifactInfo;
import org.onap.sdc.http.SdcConnectorClient;
import org.onap.sdc.impl.DistributionClientDownloadResultImpl;
import org.onap.sdc.utils.DistributionActionResultEnum;
import org.onap.ves.openapi.manager.config.DistributionClientConfig;
import org.onap.ves.openapi.manager.exceptions.ArtifactException;
import org.onap.ves.openapi.manager.model.Artifact;
import org.onap.ves.openapi.manager.service.ArtifactsCollector;
import org.onap.ves.openapi.manager.service.testModel.ArtifactInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArtifactsCollectorTest {

    private static final String NOT_VES_ARTIFACT = "NOT_VES_ARTIFACT";
    private final SdcConnectorClient sdcConnectorClient = mock(SdcConnectorClient.class);
    private final ArtifactsCollector artifactsCollector = new ArtifactsCollector(sdcConnectorClient);

    @Test
    void shouldReturnArtifactsContentWhenVESArtifactsAreDistributed() throws IOException {
        //given
        IArtifactInfo vesArtifact = new ArtifactInfo(DistributionClientConfig.VES_EVENTS_ARTIFACT_TYPE);
        List<IArtifactInfo> artifacts = List.of(vesArtifact);

        byte[] payload = getVesArtifactPayload();
        DistributionClientDownloadResultImpl artifactDownloadResult = new DistributionClientDownloadResultImpl(
                DistributionActionResultEnum.SUCCESS, "sample-message", "artifact-name",
                payload);
        when(sdcConnectorClient.downloadArtifact(vesArtifact)).thenReturn(artifactDownloadResult);
        List<Artifact> expectedArtifacts = List.of(new Artifact(vesArtifact, payload));

        //when
        List<Artifact> artifactContents = artifactsCollector.pullArtifacts(artifacts);

        //then
        assertThat(artifactContents).isEqualTo(expectedArtifacts);
    }

    @Test
    void shouldNotReturnArtifactsContentWhenNoVESArtifactsAreDistributed() {
        //given
        IArtifactInfo notVesArtifact = new ArtifactInfo(NOT_VES_ARTIFACT);
        List<IArtifactInfo> artifactsDefinitions = List.of(notVesArtifact);
        List<Artifact> expectedArtifacts = List.of();

        //when
        List<Artifact> artifacts = artifactsCollector.pullArtifacts(artifactsDefinitions);

        //then
        assertThat(artifacts).isEqualTo(expectedArtifacts);
    }

    @Test
    void shouldThrowExceptionWhenArtifactNameIsEmpty() {
        //given
        IArtifactInfo vesArtifact = new ArtifactInfo(DistributionClientConfig.VES_EVENTS_ARTIFACT_TYPE);
        List<IArtifactInfo> artifacts = List.of(vesArtifact);

        byte[] payload = new byte[0];
        DistributionClientDownloadResultImpl artifactDownloadResult = new DistributionClientDownloadResultImpl(
            DistributionActionResultEnum.SUCCESS, "sample-message", "",
            payload);
        when(sdcConnectorClient.downloadArtifact(vesArtifact)).thenReturn(artifactDownloadResult);

        //when then
        assertThrows(
            ArtifactException.class,
            () -> artifactsCollector.pullArtifacts(artifacts)
        );
    }

    private byte[] getVesArtifactPayload() throws IOException {
        return Files.readAllBytes(Paths.get("src/test/resources/ves_artifact_stndDefined_events.yaml"));
    }
}
