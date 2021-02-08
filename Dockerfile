FROM onap/integration-java11:8.0.0
COPY ./src/main/resources/schema-map.json /app/schema-map.json
COPY target/ves-openapi-manager.jar /app/
ENTRYPOINT java -jar /app/ves-openapi-manager.jar