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

package org.onap.ves.openapi.manager.service.notification;


import lombok.extern.log4j.Log4j2;
import org.onap.sdc.api.notification.IArtifactInfo;
import org.onap.sdc.api.results.IDistributionClientResult;
import org.onap.sdc.impl.DistributionClientImpl;
import org.onap.sdc.utils.DistributionStatusEnum;
import org.onap.ves.openapi.manager.model.DistributionStatusMessage;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class ArtifactsCollectorStatusSender {

    private static final String DOWNLOAD_ERROR_MESSAGE = "Couldn't download artifact";
    private final DistributionClientImpl distributionClient;

    /**
     * Constructor of ArtifactsCollectorStatusSender
     * @param distributionClient DistributionClientImpl object
     */
    public ArtifactsCollectorStatusSender(DistributionClientImpl distributionClient) {
        this.distributionClient = distributionClient;
    }

    /**
     * Sends DOWNLOAD_OK status to SDC
     * @param distributionId Service distribution ID
     * @param downloadedArtifacts Artifacts which are successfully downloaded
     * @return List of IDistributionClientResult, results of sending status
     */
    public List<IDistributionClientResult> sendDownloadOk(String distributionId, List<IArtifactInfo> downloadedArtifacts) {
        return downloadedArtifacts.stream()
                .map(artifact -> {
                    DistributionStatusMessage distributionMessage = getDownloadMessage(distributionId,
                            artifact.getArtifactURL(), DistributionStatusEnum.DOWNLOAD_OK);
                    return distributionClient.sendDownloadStatus(distributionMessage);
                })
                .collect(Collectors.toList());
    }

    /**
     * Sends DOWNLOAD_ERROR status to SDC
     * @param distributionId Service distribution ID
     * @param downloadedArtifacts Artifacts which could not be downloaded
     * @return List of IDistributionClientResult, results of sending status
     */
    public List<IDistributionClientResult> sendDownloadError(String distributionId, List<IArtifactInfo> downloadedArtifacts) {
        return downloadedArtifacts.stream()
                .map(artifact -> {
                    DistributionStatusMessage distributionMessage = getDownloadMessage(distributionId,
                            artifact.getArtifactURL(), DistributionStatusEnum.DOWNLOAD_ERROR);
                    return distributionClient.sendDownloadStatus(distributionMessage, DOWNLOAD_ERROR_MESSAGE);
                })
                .collect(Collectors.toList());
    }

    private DistributionStatusMessage getDownloadMessage(String distributionId, String artifactURL, DistributionStatusEnum downloadStatus) {
        return new DistributionStatusMessage(
                artifactURL,
                distributionId,
                distributionClient.getConfiguration().getConsumerID(),
                LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli(),
                downloadStatus);
    }
}
