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

package org.onap.ves.openapi.manager.service;

import lombok.extern.log4j.Log4j2;
import org.onap.ves.openapi.manager.model.Artifact;
import org.onap.ves.openapi.manager.model.ArtifactValidationResult;
import org.onap.ves.openapi.manager.service.notification.ArtifactsCollectorStatusSender;
import org.onap.ves.openapi.manager.service.notification.ValidationStatusSender;
import org.onap.ves.openapi.manager.service.validation.ArtifactsValidator;
import org.onap.sdc.api.consumer.INotificationCallback;
import org.onap.sdc.api.notification.IArtifactInfo;
import org.onap.sdc.api.notification.INotificationData;
import org.onap.sdc.api.notification.IResourceInstance;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class ClientCallback implements INotificationCallback {

    public static final String SEPARATOR = "===================================";
    private final List<ArtifactsValidator> validators;
    private final ValidationStatusSender validationStatusSender;
    private final ArtifactsCollectorStatusSender artifactsCollectorStatusSender;
    private final ArtifactsCollector artifactsCollector;

    public ClientCallback(List<ArtifactsValidator> validators, ArtifactsCollector artifactsCollector,
                          ArtifactsCollectorStatusSender artifactsCollectorStatusSender,
                          ValidationStatusSender validationStatusSender) {
        this.validators = validators;
        this.validationStatusSender = validationStatusSender;
        this.artifactsCollectorStatusSender = artifactsCollectorStatusSender;
        this.artifactsCollector = artifactsCollector;
    }

    @Override
    public void activateCallback(INotificationData service) {
        logServiceInfo(service);
        List<Artifact> artifacts = downloadArtifacts(service);
        validate(service.getDistributionID(), artifacts);
    }

    private void logServiceInfo(INotificationData service) {
        log.info(SEPARATOR);
        log.info("Distributed service information");
        log.info("Service UUID: {}", service.getServiceUUID());
        log.info("Service name: {}", service.getServiceName());
        List<IResourceInstance> resources = service.getResources();
        log.info("Service resources:");
        resources.forEach(resource -> {
            log.info(" - Resource: {}", resource.getResourceName());
            log.info("   Artifacts:");
            resource.getArtifacts().forEach(artifact -> log.info("   - Name: {}", artifact.getArtifactName()));
        });
        log.info(SEPARATOR);
    }

    private List<Artifact> downloadArtifacts(INotificationData service) {
        List<IArtifactInfo> artifactInfos = getArtifactInfos(service);
        List<Artifact> artifacts = artifactsCollector.pullArtifacts(artifactInfos);
        artifacts.forEach(artifact -> artifactsCollectorStatusSender.sendDownloadOk(service.getDistributionID(),artifactInfos));

        return artifacts;
    }

    private List<IArtifactInfo> getArtifactInfos(INotificationData service) {
        return service.getResources().stream()
                .map(IResourceInstance::getArtifacts)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private void validate(String distributionID, List<Artifact> artifacts) {
        validators.forEach(validator -> {
            List<ArtifactValidationResult> validationResults = validator.validate(artifacts);
            validationStatusSender.send(distributionID, validationResults);
        });
    }
}
