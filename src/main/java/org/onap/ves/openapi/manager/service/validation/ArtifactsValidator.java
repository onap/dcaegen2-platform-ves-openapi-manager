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

package org.onap.ves.openapi.manager.service.validation;

import org.onap.ves.openapi.manager.model.Artifact;
import org.onap.ves.openapi.manager.model.ArtifactValidationResult;

import java.util.List;

public interface ArtifactsValidator {
    /**
     * Validates given VES_EVENT type artifacts
     * @param artifacts List of artifacts: descriptions and contents
     * @return List of ArtifactValidationResult, validation results
     */
    List<ArtifactValidationResult> validate(List<Artifact> artifacts);
}
