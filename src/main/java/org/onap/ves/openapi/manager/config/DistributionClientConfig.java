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

package org.onap.ves.openapi.manager.config;

import lombok.Getter;
import lombok.Setter;
import org.onap.sdc.api.consumer.IConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "vesopenapimanager.distribution")
@Getter
@Setter
public class DistributionClientConfig implements IConfiguration {

    public static final String VES_EVENTS_ARTIFACT_TYPE = "VES_EVENTS";

    private String asdcAddress;
    private String msgBusAddress;
    private String user;
    private String password;
    private Integer pollingInterval;
    private Integer pollingTimeout;
    private String consumerGroup;
    private String environmentName;
    private String consumerID;
    private Boolean activateServerTLSAuth;
    private Boolean isFilterInEmptyResources;
    private Boolean isUseHttpsWithDmaap;

    @Override
    public String getAsdcAddress() {
        return asdcAddress;
    }

    @Override
    public List<String> getMsgBusAddress() {
        return Collections.singletonList(msgBusAddress);
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public int getPollingInterval() {
        return pollingInterval;
    }

    @Override
    public int getPollingTimeout() {
        return pollingTimeout;
    }

    @Override
    public List<String> getRelevantArtifactTypes() {
        return Collections.singletonList(VES_EVENTS_ARTIFACT_TYPE);
    }

    @Override
    public String getConsumerGroup() {
        return consumerGroup;
    }

    @Override
    public String getEnvironmentName() {
        return environmentName;
    }

    @Override
    public String getConsumerID() {
        return consumerID;
    }

    @Override
    public String getKeyStorePath() {
        return null;
    }

    @Override
    public String getKeyStorePassword() {
        return null;
    }

    @Override
    public boolean activateServerTLSAuth() {
        return activateServerTLSAuth;
    }

    @Override
    public boolean isFilterInEmptyResources() {
        return isFilterInEmptyResources;
    }

    @Override
    public Boolean isUseHttpsWithDmaap() {
        return isUseHttpsWithDmaap;
    }
}
