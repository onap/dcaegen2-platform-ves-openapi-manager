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

import org.junit.jupiter.api.Test;
import org.onap.sdc.api.consumer.IConfiguration;
import org.onap.sdc.api.results.IDistributionClientResult;
import org.onap.sdc.impl.DistributionClientImpl;
import org.onap.sdc.impl.DistributionClientResultImpl;
import org.onap.sdc.utils.DistributionActionResultEnum;
import org.onap.ves.openapi.manager.config.DistributionClientConfig;
import org.onap.ves.openapi.manager.model.ArtifactValidationResult;
import org.onap.ves.openapi.manager.service.testModel.ArtifactInfo;
import org.onap.ves.openapi.manager.service.validation.ArtifactsValidator;
import org.onap.ves.openapi.manager.service.validation.SchemaReferenceValidator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValidationStatusSenderTest {

    private final ArtifactsValidator validator = mock(SchemaReferenceValidator.class);
    private final IConfiguration configuration = mock(IConfiguration.class);
    private final DistributionClientImpl distributionClient = mock(DistributionClientImpl.class);
    private final ValidationStatusSender statusSender = new ValidationStatusSender(distributionClient);

    @Test
    void shouldSendValidationStatusForEachValidationResult() {
        //given
        String distributionId = "distribution-id";
        String consumerId = "ves-consumer";

        DistributionClientResultImpl sendingResult =
                new DistributionClientResultImpl(DistributionActionResultEnum.SUCCESS, "sample-message");
        List<IDistributionClientResult> expectedSendingResults = List.of(sendingResult, sendingResult);

        ArtifactInfo artifact = new ArtifactInfo(DistributionClientConfig.VES_EVENTS_ARTIFACT_TYPE);
        List<ArtifactValidationResult> validationResults = getValidationResults(artifact);

        when(distributionClient.sendDeploymentStatus(any())).thenReturn(sendingResult);
        when(distributionClient.sendDeploymentStatus(any(), anyString())).thenReturn(sendingResult);
        when(distributionClient.getConfiguration()).thenReturn(configuration);
        when(configuration.getConsumerID()).thenReturn(consumerId);

        //when
        List<IDistributionClientResult> sendingResults = statusSender.send(distributionId, validationResults);

        //then
        assertThat(sendingResults).isEqualTo(expectedSendingResults);

    }

    private List<ArtifactValidationResult> getValidationResults(ArtifactInfo artifactInfo) {
        ArtifactValidationResult validationResultValid = new ArtifactValidationResult(artifactInfo, true,
                "sample-message", validator);
        ArtifactValidationResult validationResultInvalid = new ArtifactValidationResult(artifactInfo, false,
                "sample-message", validator);
        return List.of(validationResultValid, validationResultInvalid);
    }

}