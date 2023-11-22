#!/bin/bash

echo "Starting services using a single docker compose command..."

# List of service names and their docker-compose files
services=(
    "payment-gateway:payment-gateway/docker-compose.yml"
    "customer-care:customer-care/docker-compose.yml"
    "fees-calculator:fees-calculator/docker-compose.yml"
    "mock-credit-card-network:mock-credit-card-network/docker-compose.yml"
    "business-integrator:business-integrator/docker-compose.yml"
    "payment-settlement:payment-settlement/docker-compose.yml"
    "payment-processor:payment-processor/docker-compose.yml"
    "transactions-service:transactions-service/docker-compose.yml"
    "analytics-service:analytics-service/docker-compose.yml"
    "external-bank:external-bank/docker-compose.yml"

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
#docker compose  --env-file ./.env \
#          --file payment-gateway/docker-compose.yml\
#          --file fees-calculator/docker-compose.yml\
#          --file customer-care/docker-compose.yml \
#          --file external-bank/docker-compose.yml \
#          --file mock-credit-card-network/docker-compose.yml \
#          --file business-integrator/docker-compose.yml \
#          --file payment-settlement/docker-compose.yml\
#          --file payment-processor/docker-compose.yml\
#          --file transactions-service/docker-compose.yml \
#          --file analytics-service/docker-compose.yml  up -d
               
echo "All services started."