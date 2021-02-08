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
import org.onap.sdc.api.results.IDistributionClientResult;
import org.onap.sdc.impl.DistributionClientImpl;
import org.onap.sdc.utils.DistributionStatusEnum;
import org.onap.ves.openapi.manager.model.FinalDistributionStatusMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * FinalStatusSender - sender of FinalDistributionStatusMessage with final status to SDC
 */
@Log4j2
@Service
public class FinalStatusSender {

    private static final String INVALID_ARTIFACTS_MESSAGE = "At least one VES_EVENT artifact is invalid";
    private final DistributionClientImpl distributionClient;

    /**
     * Constructor of FinalStatusSender
     * @param distributionClient DistributionClientImpl object
     */
    @Autowired
    public FinalStatusSender(DistributionClientImpl distributionClient) {
        this.distributionClient = distributionClient;
    }

    /**
     * Sends final message COMPONENT_DONE_OK to SDC
     * @param distributionId Service distribution ID
     * @return IDistributionClientResult, result of sending status
     */
    public IDistributionClientResult sendFinalStatusOk(String distributionId) {
        DistributionStatusEnum okStatus = DistributionStatusEnum.COMPONENT_DONE_OK;
        FinalDistributionStatusMessage message = getMessage(distributionId, okStatus);
        log.info("All VES_EVENT artifacts are valid, sending final status {}", okStatus.name());
        return distributionClient.sendFinalDistrStatus(message);
    }

    /**
     * Sends final message COMPONENT_DONE_ERROR to SDC
     * @param distributionId Service distribution ID
     * @return IDistributionClientResult, result of sending status
     */
    public IDistributionClientResult sendFinalStatusError(String distributionId) {
        DistributionStatusEnum errorStatus = DistributionStatusEnum.COMPONENT_DONE_ERROR;
        FinalDistributionStatusMessage message = getMessage(distributionId, errorStatus);
        log.info("At least one VES_EVENT artifact is invalid, sending final status {}", errorStatus.name());
        return distributionClient.sendFinalDistrStatus(message, INVALID_ARTIFACTS_MESSAGE);
    }

    private FinalDistributionStatusMessage getMessage(String distributionId, DistributionStatusEnum distributionStatus) {
        return new FinalDistributionStatusMessage(
                distributionId,
                LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli(),
                distributionStatus,
                distributionClient.getConfiguration().getConsumerID());
    }

}
