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

import lombok.AllArgsConstructor;
import org.onap.sdc.api.consumer.IDistributionStatusMessage;
import org.onap.sdc.utils.DistributionStatusEnum;

@AllArgsConstructor
public class DistributionStatusMessage implements IDistributionStatusMessage {

    private final String artifactUrl;
    private final String distributionId;
    private final String consumerId;
    private final long timestamp;
    private final DistributionStatusEnum status;

    @Override
    public String getArtifactURL() {
        return artifactUrl;
    }

    @Override
    public String getDistributionID() {
        return distributionId;
    }

    @Override
    public String getConsumerID() {
        return consumerId;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public DistributionStatusEnum getStatus() {
        return status;
    }
}
