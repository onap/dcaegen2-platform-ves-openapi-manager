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

package org.onap.ves.openapi.manager.service;

import lombok.extern.log4j.Log4j2;
import org.onap.ves.openapi.manager.exceptions.ArtifactException;
import org.onap.ves.openapi.manager.model.Artifact;
import org.onap.ves.openapi.manager.service.validation.SchemaReferenceValidator;
import org.onap.sdc.api.notification.IArtifactInfo;
import org.onap.sdc.http.SdcConnectorClient;
import org.onap.sdc.impl.DistributionClientDownloadResultImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Component
public class ArtifactsCollector {

    private static final String QUOTATION_MARK = "\"";

    private final SdcConnectorClient sdcConnectorClient;

    @Autowired
    public ArtifactsCollector(SdcConnectorClient sdcConnectorClient) {
        this.sdcConnectorClient = sdcConnectorClient;
    }

    public List<Artifact> pullArtifacts(List<IArtifactInfo> artifacts) {
        log.info("Downloading artifacts");
        List<IArtifactInfo> artifactDescriptions = filterVesEventsArtifacts(artifacts);
        return artifactDescriptions.stream()
                .map(sdcConnectorClient::downloadArtifact)
                .map(downloadResult -> mapDownloadResultToArtifactContent(downloadResult, artifactDescriptions))
                .collect(Collectors.toList());
    }

    private List<IArtifactInfo> filterVesEventsArtifacts(List<IArtifactInfo> artifacts) {
        return artifacts.stream()
                .filter(artifactInfo ->
                        artifactInfo.getArtifactType().equals(SchemaReferenceValidator.VES_EVENTS_ARTIFACT_TYPE))
                .collect(Collectors.toList());
    }

    private Artifact mapDownloadResultToArtifactContent(DistributionClientDownloadResultImpl downloadResult,
                                                        List<IArtifactInfo> artifactDescriptions) {
        String artifactName = parseArtifactName(downloadResult);
        Optional<IArtifactInfo> artifact = findArtifactDescription(artifactDescriptions, artifactName);
        return artifact.map(artifactInfo ->
                new Artifact(artifactInfo, downloadResult.getArtifactPayload()))
                .orElseThrow(ArtifactException::new);
    }

    private String parseArtifactName(DistributionClientDownloadResultImpl artifact) {
        String[] name = artifact.getArtifactName().split(QUOTATION_MARK);
        return (name.length < 2 ? name[0] : name[1]);
    }

    private Optional<IArtifactInfo> findArtifactDescription(List<IArtifactInfo> artifacts, String artifactName) {
        return artifacts.stream().filter(a -> a.getArtifactName().equals(artifactName)).findFirst();
    }
}
