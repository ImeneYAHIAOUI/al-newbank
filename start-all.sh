#!/bin/bash

echo "Starting services using a single docker compose command..."

docker compose \
  --env-file ./mock-credit-card-network/.env -f mock-credit-card-network/docker-compose.yml \
  --env-file ./customer-care/.env -f customer-care/docker-compose.yml \
  --env-file ./external-bank/.env -f external-bank/docker-compose.yml \
  --env-file ./payment-processor/.env -f payment-processor/docker-compose.yml \
  --env-file ./payment-gateway/.env -f payment-gateway/docker-compose.yml up -d

echo "All services started."