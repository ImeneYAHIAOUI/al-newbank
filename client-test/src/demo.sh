#!/bin/bash
# création compte client
url="http://localhost:5003/api/costumer"
data='{
  "firstName": "valentin",
  "lastName": "doe",
  "email": "valentin.doe@gmail.com",
  "phoneNumber": "0667995895",
  "BirthDate": "1990-01-01",
  "FiscalCountry": "France",
  "address": "123 Main Street"
}'
response=$(curl -s -H "Content-Type: application/json" -d "$data" "$url")

    # Extract ID from the response
    clientId=$(echo "$response" | grep -o '"id":[0-9]*' | cut -d: -f2 | head -1)

    if [ "$?" -eq 0 ]; then
    echo -e "\033[0;34mID client:\033[0m \033[0;32m$clientId\033[0m"
    debitCardUrl="http://localhost:5003/api/costumer/$clientId/virtualCard/credit"
    debitCardResponse=$(curl -s -X POST "$debitCardUrl")
    cardNumber=$(echo "$debitCardResponse" | grep -oi '"cardNumber":"[^"]*' | cut -d'"' -f4)
    cvv=$(echo "$debitCardResponse" | grep -oi '"cvv":"[^"]*' | cut -d'"' -f4)
    expiryDate=$(echo "$debitCardResponse" | grep -oi '"expiryDate":"[^"]*' | cut -d'"' -f4)
 operation='{
   "amount": 10000000,
   "operation": "deposit"
 }'
 fundsUrl="http://localhost:5003/api/costumer/$clientId/funds"
 response=$(curl -s -o /dev/null -w "%{http_code}" -X PUT -H "Content-Type: application/json" -d "$operation" "$fundsUrl")

    # Couleurs ANSI
    RED='\033[0;31m'
    GREEN='\033[0;32m'
    BLUE='\033[0;34m'
    NC='\033[0m' # No Color

    echo -e "${BLUE}Card Number:${NC} ${GREEN}$cardNumber${NC}"
    echo -e "${BLUE}CVV:${NC} ${GREEN}$cvv${NC}"
    echo -e "${BLUE}Expiry Date:${NC} ${GREEN}$expiryDate${NC}"
else
    echo -e "\033[0;31mErreur lors de la création du compte client. Code de réponse HTTP : $response\033[0m"
fi


# création compte marchand

url="http://localhost:5003/api/costumer"
data='{
  "firstName": "Christophe",
  "lastName": "gazzeh",
  "email": "Christophe.gazzeh@gmail.com",
  "phoneNumber": "0755877907",
  "BirthDate": "1980-01-01",
  "FiscalCountry": "France",
  "address": "123 boulevard wilson"
}'
response=$(curl -s -X POST -H "Content-Type: application/json" -d "$data" "$url")
id=$(echo "$response" | grep -o '"id":[0-9]*' | cut -d: -f2 | head -1)
upgradeUrl="http://localhost:5003/api/costumer/$id/upgrade"
upgradeAccountResponse=$(curl -s -X POST "$upgradeUrl")

iban=$(echo "$upgradeAccountResponse" | grep -oi '"iban":"[^"]*' | cut -d'"' -f4)
bic=$(echo "$upgradeAccountResponse" | grep -oi '"bic":"[^"]*' | cut -d'"' -f4)

randomName=$(openssl rand -hex 4)
merchantName="cookie_factory_$randomName"

# Partie aléatoire pour l'e-mail du marchand
randomEmail=$(shuf -i 1000-9999 -n 1)
merchantEmail="cookie.factory$randomEmail@gmail.com"

upgradeUrl="http://localhost:5003/api/costumer/$id/upgrade"
upgradeAccountResponse=$(curl -s -X POST "$upgradeUrl")

iban=$(echo "$upgradeAccountResponse" | grep -oi '"iban":"[^"]*' | cut -d'"' -f4)
bic=$(echo "$upgradeAccountResponse" | grep -oi '"bic":"[^"]*' | cut -d'"' -f4)
merchant='{
  "name": "'"$merchantName"'",
  "email": "'"$merchantEmail"'",
  "bankAccount": {
   "iban": "'"$iban"'",
      "bic": "'"$bic"'"
  }
}'
echo -e "\033[0;34mMerchant:\033[0m \033[0;32m$merchant\033[0m"

url="http://localhost:5012/api/integration/merchants"
response=$(curl -s -X POST -H "Content-Type: application/json" -d "$merchant" "$url")
merchantId=$(echo "$response" | grep -o '"id":[0-9]*' | cut -d: -f2 | head -2 | tail -1)
echo -e "\033[0;34mID marchand:\033[0m \033[0;32m$merchantId\033[0m"

# Création d'une application avec des éléments prédéfinis
randomAppName=$(openssl rand -hex 4)
appName="cookie_factory_app_$randomAppName"

# Partie aléatoire pour l'e-mail de l'application
randomAppEmail=$(shuf -i 1000-9999 -n 1)
appEmail="cookie.factory.app$randomAppEmail@gmail.com"

# Partie aléatoire pour la description de l'application
randomDescription=$(openssl rand -hex 4)
appDescription="Cookie Factory App - $randomDescription"
appUrl="http://$appName.com"

applicationIntegrationDto='{
  "name": "'"$appName"'",
  "email": "'"$appEmail"'",
  "url": "'"$appUrl"'",
  "description": "'"$appDescription"'",
  "merchantId":  "'"$merchantId"'"
}'
echo -e "\033[0;34mApplication:\033[0m \033[0;32m$applicationIntegrationDto\033[0m"

url="http://localhost:5012/api/integration/applications"
response=$(curl -s -X POST -H "Content-Type: application/json" -d "$applicationIntegrationDto" "$url")
ApplicationId=$(echo "$response" | grep -o '"id":[0-9]*' | cut -d: -f2 | head -1)
echo -e "\033[0;34mID Application:\033[0m \033[0;32m$ApplicationId\033[0m"

apiKey=$(echo "$response" | grep -o '"apiKey":"[^"]*' | cut -d'"' -f4)
echo -e "\033[0;34mAPI Key:\033[0m \033[0;32m$apiKey\033[0m"

sed -i "s/NEWBANK_TOKEN=.*/NEWBANK_TOKEN=${apiKey}/" newbank-example/.env

# shellcheck disable=SC2164
cd newbank-example

npm run start&

# Wait for the service to start
while ! nc -z localhost 6906; do
    sleep 1
done

paymentDto='{
     "cardNumber": "'"${cardNumber}"'",
     "cvv": "'"${cvv}"'",
      "expirationDate": "'"${expiryDate}"'",
      "amount": 500,
}'

curl -s -X POST -H "Content-Type: application/json" -d "$paymentDto" "http://localhost:6906/payment" -w "%{http_code}" >/dev/null

