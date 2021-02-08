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

@Log4j2
@Service
public class FinalStatusSender {

    public static final String INVALID_ARTIFACTS_MESSAGE = "At least one VES_EVENT artifact is invalid";
    private final DistributionClientImpl distributionClient;

    @Autowired
    public FinalStatusSender(DistributionClientImpl distributionClient) {
        this.distributionClient = distributionClient;
    }

    public IDistributionClientResult sendFinalStatusOk(String distributionId) {
        DistributionStatusEnum okStatus = DistributionStatusEnum.COMPONENT_DONE_OK;
        FinalDistributionStatusMessage message = getMessage(distributionId, okStatus);
        log.info("All VES_EVENT artifacts are valid, sending final status {}", okStatus.name());
        return distributionClient.sendFinalDistrStatus(message);
    }

    public IDistributionClientResult sendFinalStatusError(String distributionId) {
        DistributionStatusEnum errorStatus = DistributionStatusEnum.COMPONENT_DONE_ERROR;
        FinalDistributionStatusMessage message = getMessage(distributionId, errorStatus);
        log.info("At least  VES_EVENT artifact is invalid, sending final status {}", errorStatus.name());
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
