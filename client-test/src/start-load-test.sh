#!/bin/bash
# création compte client

#!/bin/bash
# création compte client

# création compte marchand

url="http://localhost:5003/api/costumer"
data='{
  "firstName": "Christodphe",
  "lastName": "gazdzeh"docj,
  "email": "Christodqdphe.gazzeh@gmail.com",
  "phoneNumber": "0755877907",
  "BirthDate": "1980-01-01",
  "FiscalCountry": "France",
  "address": "123 boulevard wilson"
}'
response=$(curl -s -X POST -H "Content-Type: application/json" -d "$data" "$url")
echo -e $response

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

url="http://localhost:5012/api/integration/applications"
response=$(curl -s -X POST -H "Content-Type: application/json" -d "$applicationIntegrationDto" "$url")
ApplicationId=$(echo "$response" | grep -o '"id":[0-9]*' | cut -d: -f2 | head -1)

apiKey=$(echo "$response" | grep -o '"apiKey":"[^"]*' | cut -d'"' -f4)
echo -e "\033[0;34mAPI Key:\033[0m \033[0;32m$apiKey\033[0m"

