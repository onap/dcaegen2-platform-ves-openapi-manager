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
package org.onap.ves.openapi.manager.service.testModel;

import org.onap.sdc.api.notification.IArtifactInfo;
import org.onap.sdc.api.notification.INotificationData;
import org.onap.sdc.api.notification.IResourceInstance;

import java.util.ArrayList;
import java.util.List;

public class Service implements INotificationData {

    private String distributionID;
    private String serviceName;
    private String serviceVersion;
    private String serviceUUID;
    private String serviceDescription;
    private String serviceInvariantUUID;
    private List<Resource> resources;
    private List<ArtifactInfo> serviceArtifacts;
    private String workloadContext;

    public Service(int numberOfArtifacts) {
        this.resources = List.of(new Resource(numberOfArtifacts));
        this.serviceUUID = "UUID";
        this.serviceName = "testService";
    }

    public Service() {
        this.resources = List.of(new Resource());
        this.serviceUUID = "UUID";
        this.serviceName = "testService";
    }

    @Override
    public String getDistributionID() {
        return distributionID;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public String getServiceVersion() {
        return serviceVersion;
    }

    @Override
    public String getServiceUUID() {
        return serviceUUID;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    @Override
    public String getWorkloadContext() {
        return workloadContext;
    }

    @Override
    public void setWorkloadContext(String workloadContext) {
        this.workloadContext = workloadContext;
    }

    @Override
    public String toString() {
        return "NotificationDataImpl [distributionID=" + distributionID + ", serviceName=" + serviceName
                + ", serviceVersion=" + serviceVersion + ", serviceUUID=" + serviceUUID + ", serviceDescription="
                + serviceDescription + ", serviceInvariantUUID=" + serviceInvariantUUID + ", resources=" + resources
                + ", serviceArtifacts=" + serviceArtifacts + ", workloadContext=" + workloadContext + "]";
    }

    @Override
    public List<IResourceInstance> getResources() {
        return List.copyOf(resources);
    }

    @Override
    public List<IArtifactInfo> getServiceArtifacts() {
        return List.copyOf(serviceArtifacts);
    }

    @Override
    public String getServiceInvariantUUID() {
        return serviceInvariantUUID;
    }

    @Override
    public IArtifactInfo getArtifactMetadataByUUID(String artifactUUID) {
        IArtifactInfo ret = findArtifactInfoByUUID(artifactUUID, serviceArtifacts);
        if (ret == null && resources != null) {
            for (Resource currResourceInstance : resources) {
                ret = findArtifactInfoByUUID(artifactUUID, currResourceInstance.getArtifactsImpl());
                if (ret != null) {
                    break;
                }
            }
        }
        return ret;
    }

    private IArtifactInfo findArtifactInfoByUUID(String artifactUUID, List<ArtifactInfo> listToCheck) {
        IArtifactInfo ret = null;
        if (listToCheck != null) {
            for (IArtifactInfo curr : listToCheck) {
                if (curr.getArtifactUUID().equals(artifactUUID)) {
                    ret = curr;
                    break;
                }
            }
        }
        return ret;
    }
}
