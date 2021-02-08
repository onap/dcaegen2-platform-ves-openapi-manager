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

import lombok.extern.log4j.Log4j2;
import org.onap.sdc.api.results.IDistributionClientResult;
import org.onap.sdc.impl.DistributionClientImpl;
import org.onap.sdc.utils.DistributionStatusEnum;
import org.onap.ves.openapi.manager.model.ArtifactValidationResult;
import org.onap.ves.openapi.manager.model.DistributionStatusMessage;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class ValidationStatusSender {

    private final DistributionClientImpl distributionClient;

    public ValidationStatusSender(DistributionClientImpl distributionClient) {
        this.distributionClient = distributionClient;
    }

    public List<IDistributionClientResult> send(String distributionId, List<ArtifactValidationResult> validationResults) {
        return validationResults.stream()
                .map(validationResult -> sendValidationResult(distributionId, validationResult))
                .collect(Collectors.toList());
    }

    private IDistributionClientResult sendValidationResult(String distributionId, ArtifactValidationResult validationResult) {
        DistributionStatusMessage distributionMessage = getDistributionMessage(distributionId, validationResult);
        String artifactName = validationResult.getArtifact().getArtifactName();

        if (validationResult.isValid()) {
            log.warn("Artifact {} is valid", artifactName);
            return distributionClient.sendDeploymentStatus(distributionMessage);
        } else {
            String message = validationResult.getMessage();
            log.warn("Artifact {} is invalid", artifactName);
            log.warn("Validation message: {}", message);
            return distributionClient.sendDeploymentStatus(distributionMessage, message);
        }
    }

    private DistributionStatusMessage getDistributionMessage(String distributionId, ArtifactValidationResult validationResult) {
        String artifactURL = validationResult.getArtifact().getArtifactURL();
        DistributionStatusEnum status = getErrorStatus(validationResult);
        return new DistributionStatusMessage(artifactURL, distributionId,
                distributionClient.getConfiguration().getConsumerID(),
                LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli(), status);
    }

    private DistributionStatusEnum getErrorStatus(ArtifactValidationResult validationResult) {
        return validationResult.isValid() ? DistributionStatusEnum.DEPLOY_OK : DistributionStatusEnum.DEPLOY_ERROR;
    }
}
