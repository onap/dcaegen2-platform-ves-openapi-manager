/*
 * ============LICENSE_START=======================================================
 * VES-OPENAPI-MANAGER
 * ================================================================================
 * Copyright (C) 2021 Nokia. All rights reserved.
 * Copyright © 2022-2023 Nordix Foundation. All rights reserved.
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

/**
 * DistributionClientConfig - properties required by DistributionClientImpl, values mapped from application.yml
 */
@Configuration
@ConfigurationProperties(prefix = "vesopenapimanager.distribution")
@Getter
@Setter
public class DistributionClientConfig implements IConfiguration {

    public static final String VES_EVENTS_ARTIFACT_TYPE = "VES_EVENTS";

    private String sdcAddress;
    private String user;
    private String password;
    private Integer pollingInterval;
    private Integer pollingTimeout;
    private String consumerGroup;
    private String environmentName;
    private String consumerID;
    private Boolean activateServerTLSAuth;
    private Boolean isUseHttpsWithSDC;
    private Boolean isFilterInEmptyResources;
    private String httpProxyHost;
    private int httpProxyPort;
    private String httpsProxyHost;
    private int httpsProxyPort;

    @Override
    public String getSdcAddress() {
        return sdcAddress;
    }

    @Override
    public Boolean isUseHttpsWithSDC() {
        return isUseHttpsWithSDC;
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
    public String getHttpProxyHost() {
        return httpProxyHost;
    }

    @Override
    public int getHttpProxyPort() {
        return httpProxyPort;
    }

    @Override
    public String getHttpsProxyHost() {
        return httpsProxyHost;
    }

    @Override
    public int getHttpsProxyPort() {
        return httpsProxyPort;
    }
}
