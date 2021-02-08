FROM openjdk:11-jre-slim
COPY ./src/main/resources/schema-map.json /app/schema-map.json
COPY target/ves-openapi-manager.jar /app/
ENTRYPOINT java -jar /app/ves-openapi-manager.jar