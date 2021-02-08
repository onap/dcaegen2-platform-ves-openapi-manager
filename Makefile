all: build docker run cleanup

COMPOSE = docker-compose
MVN = mvn
DOCKER = docker

build:
	@echo "Building ves-openapi-manager"
	@$(MVN) clean package

docker:
	@echo "Building ves-openapi-manager docker image"
	@$(DOCKER) build --tag ves-openapi-manager:latest .

run:
	@echo "Starting ves-openapi-manager docker containers"
	@$(COMPOSE) --file ./docker-compose.yml up

cleanup:
	@echo "Cleaning up ves-openapi-manager project, removing containers"
	@rm -rf ./target
	@$(COMPOSE) --file ./docker-compose.yml down
