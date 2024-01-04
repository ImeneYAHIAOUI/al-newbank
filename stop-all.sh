#!/bin/bash


# docker-compose -f docker-compose.yml down
docker-compose --env-file ./.env \
               -f payment-gateway-authorizer/docker-compose.yml \
               -f payment-gateway-confirmation/docker-compose.yml \
               -f fees-calculator/docker-compose.yml \
               -f customer-care/docker-compose.yml \
               -f external-bank/docker-compose.yml \
               -f mock-credit-card-network/docker-compose.yml \
               -f business-integrator/docker-compose.yml \
               -f payment-settlement/docker-compose.yml \
               -f payment-processor/docker-compose.yml \
               -f transactions-service/docker-compose.yml \
               -f analytics-service/docker-compose.yml down
               

echo "Done"

echo "Removing network"

# docker network rm spring-newbank-network
