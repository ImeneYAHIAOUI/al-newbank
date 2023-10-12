#!/bin/bash

echo "stopping sdk service"
docker-compose --env-file ./.env.docker \
               --file docker-compose-sdk.yml down
