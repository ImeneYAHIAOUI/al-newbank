#!/bin/bash

echo "Starting services using a single docker compose command..."

# List of service names and their docker-compose files
services=(
    "payment-gateway:payment-gateway/docker-compose.yml"
    "customer-care:customer-care/docker-compose.yml"
    "fees-calculator:fees-calculator/docker-compose.yml"
    "external-bank:external-bank/docker-compose.yml"
    "mock-credit-card-network:mock-credit-card-network/docker-compose.yml"
    "business-integrator:business-integrator/docker-compose.yml"
    "payment-settlement:payment-settlement/docker-compose.yml"
    "payment-processor:payment-processor/docker-compose.yml"
    "transactions-service:transactions-service/docker-compose.yml"
    "analytics-service:analytics-service/docker-compose.yml"
)

container_ids=()
#
network="spring-newbank-network"
echo "Creating network $network"
docker network create $network

echo "starting all"

docker compose -f docker-compose.yml up -d

docker compose --env-file ./.env \
          --file payment-gateway/docker-compose.yml\
          --file payment-gateway-confirmation/docker-compose.yml\
          --file fees-calculator/docker-compose.yml\
          --file customer-care/docker-compose.yml \
          --file external-bank/docker-compose.yml \
          --file mock-credit-card-network/docker-compose.yml \
          --file business-integrator/docker-compose.yml \
          --file payment-settlement/docker-compose.yml\
          --file payment-processor/docker-compose.yml\
          --file transactions-service/docker-compose.yml \
          --file analytics-service/docker-compose.yml  up -d
               
echo "All services started."