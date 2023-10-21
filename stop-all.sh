#!/bin/bash


services=(
    "payment-processor:payment-processor/docker-compose.yml"
    "customer-care:customer-care/docker-compose.yml"
    "mock-credit-card-network:mock-credit-card-network/docker-compose.yml"
    "payment-gateway:payment-gateway/docker-compose.yml"
    "fees-calculator:fees-calculator/docker-compose.yml"
    "transactions-service:transactions-service/docker-compose.yml"
    "newbank:docker-compose.yml"
)

container_ids=()

echo "stopping all"

stop_service() {
    local service_name=$1
    local compose_file=$2
    echo "Stopping service $service_name..."
    docker compose --env-file ./$service_name/.env -f $compose_file down
}

# Loop to stop all services

for service in "${services[@]}"; do
    IFS=':' read -ra service_info <<< "$service"
    service_name=${service_info[0]}
    compose_file=${service_info[1]}

    stop_service "$service_name" "$compose_file"
done

echo "Done"

echo "Removing network"

docker network rm spring-newbank-network