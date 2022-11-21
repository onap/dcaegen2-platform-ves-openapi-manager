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

package org.onap.ves.openapi.manager;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.onap.sdc.api.results.IDistributionClientResult;
import org.onap.sdc.http.HttpClientFactory;
import org.onap.sdc.http.HttpRequestFactory;
import org.onap.sdc.http.HttpSdcClient;
import org.onap.sdc.http.SdcConnectorClient;
import org.onap.sdc.impl.DistributionClientImpl;
import org.onap.ves.openapi.manager.config.DistributionClientConfig;
import org.onap.ves.openapi.manager.service.ArtifactsCollector;
import org.onap.ves.openapi.manager.service.ClientCallback;
import org.onap.ves.openapi.manager.service.notification.ArtifactsCollectorStatusSender;
import org.onap.ves.openapi.manager.service.notification.FinalStatusSender;
import org.onap.ves.openapi.manager.service.notification.ValidationStatusSender;
import org.onap.ves.openapi.manager.service.validation.ArtifactsValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@Log4j2
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    /**
     * Creates bean of SdcConnectorClient
     * @param clientConfig DistributionClientConfig object with configuration properties
     * @return SdcConnectorClient
     */
    @Bean
    public SdcConnectorClient getSdcConnectorClient(DistributionClientConfig clientConfig) {
        HttpRequestFactory requestFactory = new HttpRequestFactory(clientConfig.getUser(), clientConfig.getPassword());
        HttpClientFactory clientFactory = new HttpClientFactory(clientConfig);
        return new SdcConnectorClient(clientConfig, new HttpSdcClient(clientConfig.getSdcAddress(), clientFactory, requestFactory));
    }

    /**
     * Creates bean of DistributionClientImpl
     * @param clientConfig DistributionClientConfig object with configuration properties
     * @param validators List of objects implementing ArtifactValidator interface
     * @param artifactsCollector ArtifactsCollector object which downloads artifacts contents
     * @return DistributionClientImpl
     */
    @Bean
    public DistributionClientImpl getDistributionClientImpl(DistributionClientConfig clientConfig,
                                                            List<ArtifactsValidator> validators,
                                                            ArtifactsCollector artifactsCollector) {
        DistributionClientImpl client = new DistributionClientImpl();
        ClientCallback callback = new ClientCallback(validators, artifactsCollector,
                new ArtifactsCollectorStatusSender(client), new ValidationStatusSender(client),
                new FinalStatusSender(client));

        log.info(ClientCallback.SEPARATOR);
        IDistributionClientResult initResult = client.init(clientConfig, callback);
        log.info(initResult.getDistributionMessageResult());
        log.info(ClientCallback.SEPARATOR);

        log.info(ClientCallback.SEPARATOR);
        IDistributionClientResult startResult = client.start();
        log.info(startResult.getDistributionMessageResult());
        log.info(ClientCallback.SEPARATOR);

        return client;
    }

}
