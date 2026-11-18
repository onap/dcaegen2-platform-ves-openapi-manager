# Copyright (C) 2026 Deutsche Telekom AG. All rights reserved.
# SPDX-License-Identifier: Apache-2.0

FROM eclipse-temurin:17-jre-alpine

RUN addgroup -S -g 101 onap && adduser -S -u 1000 -G onap onap \
    && mkdir -p /app && chown onap:onap /app
WORKDIR /app
COPY src/main/resources/schema-map.json target/ves-openapi-manager.jar /app/
USER onap
ENTRYPOINT ["java", "-jar", "/app/ves-openapi-manager.jar"]
