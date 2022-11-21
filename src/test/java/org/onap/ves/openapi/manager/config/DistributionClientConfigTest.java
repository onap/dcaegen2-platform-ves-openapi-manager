/*
 * ============LICENSE_START=======================================================
 * VES-OPENAPI-MANAGER
 * ================================================================================
 * Copyright (C) 2021 Nokia. All rights reserved.
 * Modifications Copyright © 2022 Nordix Foundation
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

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DistributionClientConfigTest {

    @Test
    void shouldCreateDistributionClientConfig() {
        //given
        String sdcAddress = "sdc-be.onap:8443";
        String user = "dcae";
        String password = "Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U";
        Integer pollingInterval = 20;
        Integer pollingTimeout = 20;
        String consumerGroup = "ves-openapi-manager";
        String environmentName = "AUTO";
        String consumerID = "ves-openapi-manager";
        Map<String, Object> properties = new HashMap<>();
        properties.put("vesopenapimanager.distribution.sdcAddress", sdcAddress);
        properties.put("vesopenapimanager.distribution.user", user);
        properties.put("vesopenapimanager.distribution.password", password);
        properties.put("vesopenapimanager.distribution.pollingInterval", pollingInterval);
        properties.put("vesopenapimanager.distribution.pollingTimeout", pollingTimeout);
        properties.put("vesopenapimanager.distribution.consumerGroup", consumerGroup);
        properties.put("vesopenapimanager.distribution.environmentName", environmentName);
        properties.put("vesopenapimanager.distribution.consumerID", consumerID);
        properties.put("vesopenapimanager.distribution.activateServerTLSAuth", false);
        properties.put("vesopenapimanager.distribution.isFilterInEmptyResources", false);
        ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(source);

        //when
        BindResult<DistributionClientConfig> result = binder.bind("vesopenapimanager.distribution", DistributionClientConfig.class);

        //then
        assertThat(result.isBound()).isTrue();
        DistributionClientConfig config = result.get();
        assertThat(config.getSdcAddress()).isEqualTo(sdcAddress);
        assertThat(config.getUser()).isEqualTo(user);
        assertThat(config.getPassword()).isEqualTo(password);
        assertThat(config.getPollingInterval()).isEqualTo(pollingInterval);
        assertThat(config.getPollingTimeout()).isEqualTo(pollingTimeout);
        assertThat(config.getRelevantArtifactTypes()).isEqualTo(Collections.singletonList("VES_EVENTS"));
        assertThat(config.getConsumerGroup()).isEqualTo(consumerGroup);
        assertThat(config.getEnvironmentName()).isEqualTo(environmentName);
        assertThat(config.getConsumerID()).isEqualTo(consumerID);
        assertThat(config.getKeyStorePath()).isNull();
        assertThat(config.getKeyStorePassword()).isNull();
        assertThat(config.activateServerTLSAuth()).isFalse();
        assertThat(config.isFilterInEmptyResources()).isFalse();
        assertThat(config.activateServerTLSAuth()).isFalse();
    }
}
