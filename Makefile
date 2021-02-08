include environment.config

all: build docker run cleanup

port-forward:
	@echo "Forwarding ports. To cancel forwarding press CTRL+C."
	@$(SSH) -L 3904:$(WORKER_IP):$(MESSAGE_ROUTER_PORT) ubuntu@$(RKE_IP) -i $(SSH_LAB_KEY_PATH) -N

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
