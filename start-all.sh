#!/bin/bash


services=(
    "payment-processor:payment-processor/docker-compose.yml"
    "customer-care:customer-care/docker-compose.yml"
    "mock-credit-card-network:mock-credit-card-network/docker-compose.yml"
    "payment-gateway:payment-gateway/docker-compose.yml"
)

container_ids=()

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

