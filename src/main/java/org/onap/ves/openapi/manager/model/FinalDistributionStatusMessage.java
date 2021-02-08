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
import org.onap.sdc.api.consumer.IFinalDistrStatusMessage;
import org.onap.sdc.utils.DistributionStatusEnum;


/**
 * FinalDistributionStatusMessage - model class for final ves-openapi-manager's validation message sent to SDC
 */
@AllArgsConstructor
public class FinalDistributionStatusMessage implements IFinalDistrStatusMessage {

    private final String distributionId;
    private final long timestamp;
    private final DistributionStatusEnum status;
    private final String consumerId;

    @Override
    public String getDistributionID() {
        return distributionId;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public DistributionStatusEnum getStatus() {
        return status;
    }

    @Override
    public String getConsumerID() {
        return consumerId;
    }

}
