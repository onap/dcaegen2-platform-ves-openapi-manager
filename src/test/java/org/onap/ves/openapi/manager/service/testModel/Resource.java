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
import org.onap.sdc.api.notification.IResourceInstance;
import org.onap.ves.openapi.manager.config.DistributionClientConfig;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Resource implements IResourceInstance {

    private String resourceInstanceName;
    private String resourceCustomizationUUID;
    private String resourceName;
    private String resourceVersion;
    private String resourceType;
    private String resourceUUID;
    private String resourceInvariantUUID;
    private String category;
    private String subcategory;
    private List<ArtifactInfo> artifacts;

    public Resource(int numberOfArtifacts) {
        this.artifacts = Stream.generate(() -> new ArtifactInfo(DistributionClientConfig.VES_EVENTS_ARTIFACT_TYPE))
                .limit(numberOfArtifacts)
                .collect(Collectors.toList());
    }

    public Resource() {
        this.artifacts = List.of(new ArtifactInfo(DistributionClientConfig.VES_EVENTS_ARTIFACT_TYPE));
    }

    private Resource(IResourceInstance resourceInstance) {
        resourceInstanceName = resourceInstance.getResourceInstanceName();
        resourceCustomizationUUID = resourceInstance.getResourceCustomizationUUID();
        resourceName = resourceInstance.getResourceName();
        resourceVersion = resourceInstance.getResourceVersion();
        resourceType = resourceInstance.getResourceType();
        resourceUUID = resourceInstance.getResourceUUID();
        resourceInvariantUUID = resourceInstance.getResourceInvariantUUID();
        category = resourceInstance.getCategory();
        subcategory = resourceInstance.getSubcategory();
        artifacts = ArtifactInfo.convertToArtifactInfoImpl(resourceInstance.getArtifacts());
    }

    public static List<Resource> convertToJsonContainer(List<IResourceInstance> resources) {
        return resources.stream().map(Resource::new).collect(Collectors.toList());
    }

    @Override
    public String getResourceInstanceName() {
        return resourceInstanceName;
    }

    public void setResourceInstanceName(String resourceInstanceName) {
        this.resourceInstanceName = resourceInstanceName;
    }

    @Override
    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    @Override
    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    @Override
    public String getResourceUUID() {
        return resourceUUID;
    }

    public void setResourceUUID(String resourceUUID) {
        this.resourceUUID = resourceUUID;
    }

    @Override
    public List<IArtifactInfo> getArtifacts() {
        return List.copyOf(artifacts);
    }

    public void setArtifacts(List<ArtifactInfo> artifacts) {
        this.artifacts = artifacts;
    }

    public List<ArtifactInfo> getArtifactsImpl() {
        return artifacts;
    }

    @Override
    public String getResourceInvariantUUID() {
        return resourceInvariantUUID;
    }

    public void setResourceInvariantUUID(String resourceInvariantUUID) {
        this.resourceInvariantUUID = resourceInvariantUUID;
    }

    public String getResourceCustomizationUUID() {
        return resourceCustomizationUUID;
    }

    public void setResourceCustomizationUUID(String resourceCustomizationUUID) {
        this.resourceCustomizationUUID = resourceCustomizationUUID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }
}
