#!/bin/bash


echo "starting all"
docker-compose --file payment-processor/docker-compose.yml \
               --file mock-credit-card-network/docker-compose.yml up