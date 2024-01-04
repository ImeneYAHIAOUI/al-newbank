#!/bin/bash

echo "Starting services using a single docker compose command..."


network="spring-newbank-network"
echo "Creating network $network"
docker network create $network

echo "starting all"

# docker compose -f kafka/docker-compose.yml up -d


docker compose -f monitoring/docker-compose.yml up -d

docker compose --env-file ./.env \
          --file payment-gateway-confirmation/docker-compose.yml\
          --file payment-gateway-authorizer/docker-compose.yml\
          --file fees-calculator/docker-compose.yml\
          --file customer-care/docker-compose.yml \
          --file mock-credit-card-network/docker-compose.yml \
          --file metrics-service/docker-compose.yml \
          --file business-integrator/docker-compose.yml \
          --file payment-settlement/docker-compose.yml\
          --file payment-processor/docker-compose.yml\
          --file transactions-service/docker-compose.yml \
          --file status-reporter/docker-compose.yml   up -d

               
echo "All services started."
