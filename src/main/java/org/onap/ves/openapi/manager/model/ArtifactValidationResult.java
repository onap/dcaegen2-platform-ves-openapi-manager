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

package org.onap.ves.openapi.manager.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.onap.sdc.api.notification.IArtifactInfo;
import org.onap.ves.openapi.manager.service.validation.ArtifactsValidator;

/**
 * ArtifactValidationResult - model class for result of artifact validation
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ArtifactValidationResult {
    private final IArtifactInfo artifact;
    private final boolean isValid;
    private final String message;
    private final ArtifactsValidator validator;
}
