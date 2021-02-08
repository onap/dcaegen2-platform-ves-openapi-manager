package org.onap.ves.openapi.manager.model;

import lombok.AllArgsConstructor;
import org.onap.sdc.api.consumer.IFinalDistrStatusMessage;
import org.onap.sdc.utils.DistributionStatusEnum;


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
