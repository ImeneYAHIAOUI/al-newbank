#!/bin/bash


# List of service names and their docker-compose files
services=(
    "payment-gateway:payment-gateway/docker-compose.yml"
    "fees-calculator:fees-calculator/docker-compose.yml"
    "custmer-care:custmer-care/docker-compose.yml"
    "external-bank:external-bank/docker-compose.yml"
    "mock-credit-card-network:mock-credit-card-network/docker-compose.yml"
    "business-integrator:business-integrator/docker-compose.yml"
    "payment-settlement:payment-settlement/docker-compose.yml"
    "payment-processor:payment-processor/docker-compose.yml"
    "transactions-service:transactions-service/docker-compose.yml"
    "analytics-service:analytics-service/docker-compose.yml"
)
container_ids=()

start_service() {
    local service_name=$1
    local compose_file=$2
    echo "Starting service $service_name..."
    docker compose --env-file ./.env -f $compose_file up  -d
}

# Loop to start all services
for service in "${services[@]}"; do
    IFS=':' read -ra service_info <<< "$service"
    service_name=${service_info[0]}
    compose_file=${service_info[1]}

    start_service "$service_name" "$compose_file"
done



# Function to format HTTP response codes with colors
format_http_code() {
  local code=$1
  if [ "$code" == "200" ] || [ "$code" == "201" ]; then
    echo -e "\e[32mHTTP $code\e[0m"
  else
    echo -e "\e[31mHTTP $code\e[0m"
  fi
}

docker compose  --env-file ./.env \
          --file payment-gateway/docker-compose.yml\
          --file fees-calculator/docker-compose.yml\
          --file  custmer-care/docker-compose.yml \
          --file external-bank/docker-compose.yml \
          --file mock-credit-card-network/docker-compose.yml \
          --file  business-integrator/docker-compose.yml \
          --file payment-settlement/docker-compose.yml\
          --file payment-processor/docker-compose.yml\
          --file  transactions-service/docker-compose.yml \
          --file analytics-service/docker-compose.yml \
          logs --follow -t | grep -E -v 'RouterExplorer|InstanceLoader|NestFactory|NestApplication|RoutesResolver|Controller|daemon|kafkajs'

#export PAYMENT_INFO='{"cardNumber": "6920522972946995", "cvv": "127", "expirationDate": "11/2025", "amount": "50"}'
#export TOKEN='eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJOZXdCYW5rIiwic3ViIjoiQVBJIEtleSIsImV4cCI6MTY5OTI3MDEyNSwiaWQiOjMsIm5hbWUiOiJhcHAxIiwiZW1haWwiOiJ5dXktZnXDqHl0dWdAamlvLmNvbSIsInVybCI6Imhwb3BwcHVldGloIiwiZGVzY3JpcHRpb24iOiJkeXJ0c3JmdWhrIiwiZGF0ZU9mSXNzdWUiOjE2OTkyNjY1MjU2MTZ9.f72xUXAngDyR7_dfqVcSnDfifAYwYVIA770Zl8pIt9Y'

#tsc main.ts

#node -e "const paymentInfo = JSON.parse(process.env.PAYMENT_INFO); const token = process.env.TOKEN; require('./main.js').main(paymentInfo, token);"
