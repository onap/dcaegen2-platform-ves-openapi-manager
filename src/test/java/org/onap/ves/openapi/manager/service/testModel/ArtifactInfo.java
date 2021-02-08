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

import java.util.ArrayList;
import java.util.List;


public class ArtifactInfo implements IArtifactInfo {

    private final String artifactName;
    private final String artifactType;
    private final String artifactURL;
    private String artifactChecksum;
    private Integer artifactTimeout;
    private String artifactDescription;
    private String artifactVersion;
    private String artifactUUID;
    private IArtifactInfo generatedArtifact;
    private List<IArtifactInfo> relatedArtifacts;

    public ArtifactInfo(String artifactType) {
        this.artifactName = "artifact-name";
        this.artifactType = artifactType;
        this.artifactURL = "http://artifact-url:1234/test";
    }

    private ArtifactInfo(IArtifactInfo iArtifactInfo) {
        artifactName = iArtifactInfo.getArtifactName();
        artifactType = iArtifactInfo.getArtifactType();
        artifactURL = iArtifactInfo.getArtifactURL();
        artifactChecksum = iArtifactInfo.getArtifactChecksum();
        artifactDescription = iArtifactInfo.getArtifactDescription();
        artifactTimeout = iArtifactInfo.getArtifactTimeout();
        artifactVersion = iArtifactInfo.getArtifactVersion();
        artifactUUID = iArtifactInfo.getArtifactUUID();
        generatedArtifact = iArtifactInfo.getGeneratedArtifact();
        relatedArtifacts = iArtifactInfo.getRelatedArtifacts();

    }

    @Override
    public String getArtifactName() {
        return artifactName;
    }

    @Override
    public String getArtifactType() {
        return artifactType;
    }

    @Override
    public String getArtifactURL() {
        return artifactURL;
    }

    @Override
    public String getArtifactChecksum() {
        return artifactChecksum;
    }

    @Override
    public Integer getArtifactTimeout() {
        return artifactTimeout;
    }

    @Override
    public String getArtifactDescription() {
        return artifactDescription;
    }

    @Override
    public String getArtifactVersion() {
        return artifactVersion;
    }

    @Override
    public String getArtifactUUID() {
        return artifactUUID;
    }

    @Override
    public IArtifactInfo getGeneratedArtifact() {
        return generatedArtifact;
    }

    @Override
    public List<IArtifactInfo> getRelatedArtifacts() {
        return relatedArtifacts;
    }

    public static List<ArtifactInfo> convertToArtifactInfoImpl(List<IArtifactInfo> list) {
        List<ArtifactInfo> ret = new ArrayList<>();
        if (list != null) {
            for (IArtifactInfo artifactInfo : list) {
                ret.add(new ArtifactInfo(artifactInfo));
            }
        }
        return ret;
    }
}
