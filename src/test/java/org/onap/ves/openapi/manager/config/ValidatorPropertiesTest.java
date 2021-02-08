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
package org.onap.ves.openapi.manager.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidatorPropertiesTest {

    @Test
    void shouldCreateDistributionClientConfig() {
        //given
        String schemaMapPath = "schema-map.json";
        String eventDomainPath = "/event/structure/commonEventHeader/structure/domain/value";
        String eventSchemaReferencePath = "/event/structure/stndDefinedFields/structure/schemaReference/value";

        Map<String, Object> properties = new HashMap<>();
        properties.put("vesopenapimanager.validation.schemaMapPath", schemaMapPath);
        properties.put("vesopenapimanager.validation.eventDomainPath", eventDomainPath);
        properties.put("vesopenapimanager.validation.eventSchemaReferencePath", eventSchemaReferencePath);

        ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(source);

        //when
        BindResult<ValidatorProperties> result = binder.bind("vesopenapimanager.validation", ValidatorProperties.class);

        //then
        assertThat(result.isBound()).isTrue();
        ValidatorProperties config = result.get();
        assertThat(config.getSchemaMapPath()).isEqualTo(schemaMapPath);
        assertThat(config.getEventDomainPath()).isEqualTo(eventDomainPath);
        assertThat(config.getEventSchemaReferencePath()).isEqualTo(eventSchemaReferencePath);
    }
}
