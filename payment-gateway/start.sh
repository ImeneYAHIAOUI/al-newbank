#!/bin/bash
source ../framework.sh

echo "starting load-balancer..."
docker-compose --env-file ./.env.docker \
               --file docker-compose.yml up -d


