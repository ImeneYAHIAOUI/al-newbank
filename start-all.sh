#!/bin/bash

echo "Starting services using a single docker compose command..."

services=(
    "payment-processor:payment-processor/docker-compose.yml"
    "customer-care:customer-care/docker-compose.yml"
    "mock-credit-card-network:mock-credit-card-network/docker-compose.yml"
    "payment-gateway:payment-gateway/docker-compose.yml"
    "transactions:transactions/docker-compose.yml"
      "external-bank:external-bank/docker-compose.yml"
    "fees-calculator:fees-calculator/docker-compose.yml"
)

container_ids=()
#
network="spring-newbank-network"
echo "Creating network $network"
docker network create $network

docker compose -f docker-compose.yml up -d

echo "starting all"

start_service() {
    local service_name=$1
    local compose_file=$2
    echo "Starting service $service_name..."
    docker compose --env-file ./$service_name/.env -f $compose_file up  -d
}

# Loop to start all services
for service in "${services[@]}"; do
    IFS=':' read -ra service_info <<< "$service"
    service_name=${service_info[0]}
    compose_file=${service_info[1]}

    start_service "$service_name" "$compose_file"
done

echo "All services started."