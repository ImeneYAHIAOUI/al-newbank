#!/bin/bash
source ../framework.sh

echo "starting sdk-service..."
docker-compose --env-file ./.env.docker \
               --file docker-compose-sdk.yml up -d


