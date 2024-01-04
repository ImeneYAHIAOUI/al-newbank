#!/bin/bash
source ../framework.sh

echo "starting business-integrator..."
docker-compose --env-file ./.env \
               --file docker-compose.yml up -d


