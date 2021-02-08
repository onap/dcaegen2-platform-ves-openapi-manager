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

package org.onap.ves.openapi.manager.model;

import org.junit.jupiter.api.Test;
import org.onap.sdc.utils.DistributionStatusEnum;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;


public class DistributionStatusMessageTest {
    @Test
    void shouldCreateDistributionStatusMessage() {
        //given
        long timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        String consumerId = "ves-consumer";
        String artifactUrl = "http://artifact-url:1234/test";
        String distributionId = "distribution-id";

        //when
        DistributionStatusMessage distributionStatusMessage = new DistributionStatusMessage(
                artifactUrl,
                distributionId,
                consumerId,
                timestamp,
                DistributionStatusEnum.DOWNLOAD_OK);

        //then
        assertThat(distributionStatusMessage.getArtifactURL()).isEqualTo(artifactUrl);
        assertThat(distributionStatusMessage.getDistributionID()).isEqualTo(distributionId);
        assertThat(distributionStatusMessage.getConsumerID()).isEqualTo(consumerId);
        assertThat(distributionStatusMessage.getTimestamp()).isEqualTo(timestamp);
        assertThat(distributionStatusMessage.getStatus()).isEqualTo(DistributionStatusEnum.DOWNLOAD_OK);
    }
}