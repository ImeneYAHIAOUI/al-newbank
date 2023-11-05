#!/bin/bash
source ../framework.sh

echo "starting business-integrator..."
docker-compose --env-file ./.env.docker \
               --file docker-compose.yml up -d


