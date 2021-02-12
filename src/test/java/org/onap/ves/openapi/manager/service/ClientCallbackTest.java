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

package org.onap.ves.openapi.manager.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.onap.sdc.api.notification.IArtifactInfo;
import org.onap.sdc.api.notification.INotificationData;
import org.onap.ves.openapi.manager.model.Artifact;
import org.onap.ves.openapi.manager.model.ArtifactValidationResult;
import org.onap.ves.openapi.manager.service.notification.ArtifactsCollectorStatusSender;
import org.onap.ves.openapi.manager.service.notification.FinalStatusSender;
import org.onap.ves.openapi.manager.service.notification.ValidationStatusSender;
import org.onap.ves.openapi.manager.service.testModel.Service;
import org.onap.ves.openapi.manager.service.validation.ArtifactsValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ClientCallbackTest {

    private ArtifactsCollector artifactsCollector;
    private ArtifactsCollectorStatusSender artifactsCollectorStatusSender;
    private ValidationStatusSender validationStatusSender;
    private FinalStatusSender finalStatusSender;
    private ArtifactsValidator artifactsValidator;
    private List<ArtifactsValidator> validators;
    private ClientCallback callback;

    @BeforeEach
    void setUpMocks() {
        artifactsCollector = mock(ArtifactsCollector.class);
        artifactsCollectorStatusSender = mock(ArtifactsCollectorStatusSender.class);
        validationStatusSender = mock(ValidationStatusSender.class);
        finalStatusSender = mock(FinalStatusSender.class);
        artifactsValidator = mock(ArtifactsValidator.class);
        validators = new ArrayList<>();
        validators.add(artifactsValidator);

        callback = new ClientCallback(validators, artifactsCollector, artifactsCollectorStatusSender,
                validationStatusSender, finalStatusSender);
    }

    @Test
    void shouldNotValidateAnyArtifactsOrSendAnyStatusIfPulledArtifactsListIsEmpty() {
        testClientCallback(0);
    }

    @Test
    void shouldValidateArtifactUsingValidatorAndSendStatusIfPulledArtifactsListContainsOneArtifact() {
        testClientCallback(1);
    }

    @Test
    void shouldValidateArtifactsUsingValidatorAndSendStatusesIfPulledArtifactsListContainsMultipleArtifacts() {
        testClientCallback(3);
    }

    @Test
    void shouldValidateArtifactUsingMultipleValidatorsAndSendStatusIfPulledArtifactsListContainsOneArtifact() {
        ArtifactsValidator extraArtifactsValidator = mock(ArtifactsValidator.class);
        testClientCallback(1, extraArtifactsValidator);
    }

    @Test
    void shouldValidateArtifactUsingMultipleValidatorsAndSendStatusIfPulledArtifactsListContainsMultipleArtifacts() {
        ArtifactsValidator extraArtifactsValidator = mock(ArtifactsValidator.class);
        testClientCallback(3, extraArtifactsValidator);
    }

    private void testClientCallback(int numberOfPulledArtefacts, ArtifactsValidator... extraValidators) {
        // given
        validators.addAll(Arrays.asList(extraValidators));
        final INotificationData service = new Service(numberOfPulledArtefacts);
        ExpectedTestParameters testParameters = setupCallbackTest(service);

        // when
        callback.activateCallback(service);

        // then
        verifyMockedMethodCalls(testParameters, numberOfPulledArtefacts);
        for(ArtifactsValidator validator: extraValidators) {
            verify(validator, times(1)).validate(testParameters.getExpectedPulledArtifacts());
        }
    }

    private ExpectedTestParameters setupCallbackTest(INotificationData service) {
        final List<IArtifactInfo> expectedArtifactsList = service.getResources().get(0).getArtifacts();

        final List<Artifact> expectedPulledArtifacts = new ArrayList<>();
        final List<ArtifactValidationResult> expectedValidationResults = new ArrayList<>();
        for(IArtifactInfo artifactInfo: expectedArtifactsList) {
            final Artifact artifact = setUpTestArtifact(artifactInfo);
            expectedPulledArtifacts.add(artifact);
            final ArtifactValidationResult expectedValidationResult = new ArtifactValidationResult(
                artifactInfo, true, artifactInfo.getArtifactName() + " validated", artifactsValidator
            );
            expectedValidationResults.add(expectedValidationResult);
        }

        when(artifactsCollector.pullArtifacts(expectedArtifactsList)).thenReturn(expectedPulledArtifacts);
        when(artifactsValidator.validate(expectedPulledArtifacts)).thenReturn(expectedValidationResults);

        return new ExpectedTestParameters(expectedPulledArtifacts, expectedValidationResults, expectedArtifactsList);
    }

    private Artifact setUpTestArtifact(IArtifactInfo artifactInfo) {
        final byte[] expectedArtifactContent = ("test content " + artifactInfo.getArtifactName()).getBytes();
        return new Artifact(artifactInfo, expectedArtifactContent);
    }

    private void verifyMockedMethodCalls(ExpectedTestParameters testParameters, int numberOfPulledArtifacts) {
        verify(artifactsCollector, times(1)).pullArtifacts(testParameters.getExpectedArtifactsList());
        verify(artifactsCollectorStatusSender, times(numberOfPulledArtifacts)).sendDownloadOk(any(), eq(testParameters.getExpectedArtifactsList()));
        verify(validationStatusSender, times(1)).send(any(), eq(testParameters.getExpectedValidationResults()));
        validators.forEach(validator -> verify(validator, times(1)).validate(testParameters.getExpectedPulledArtifacts()));
    }

    @Getter
    @AllArgsConstructor
    private static class ExpectedTestParameters {
        final private List<Artifact> expectedPulledArtifacts;
        final private List<ArtifactValidationResult> expectedValidationResults;
        final private List<IArtifactInfo> expectedArtifactsList;
    }
}
