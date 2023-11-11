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
response=$(curl -s -o /dev/null -w "%{http_code}" -X POST -H "Content-Type: application/json" -d "$data" "$url")

if [ "$response" -eq 201 ]; then
    id=$(curl -s -X POST -H "Content-Type: application/json" -d "$data" "$url" | grep -o '"id":[0-9]*' | cut -d: -f2 | head -1)
    debitCardUrl="http://localhost:5003/api/costumer/$id/virtualCard/credit"
    debitCardResponse=$(curl -s -X POST "$debitCardUrl")
    cardNumber=$(echo "$debitCardResponse" | grep -oi '"cardNumber":"[^"]*' | cut -d'"' -f4)
    cvv=$(echo "$debitCardResponse" | grep -oi '"cvv":"[^"]*' | cut -d'"' -f4)
    expiryDate=$(echo "$debitCardResponse" | grep -oi '"expiryDate":"[^"]*' | cut -d'"' -f4)

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
#!/bin/bash
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
merchant='{
  "name": "cookie factory",
  "email": "cookie.factory@gmail.com",
  "bankAccount": {
   "IBAN": "'"$iban"'",
      "BIC": "'"$bic"'"
  }
}'
echo -e "\033[0;34mMerchant:\033[0m \033[0;32m$merchant\033[0m"

url="http://localhost:5012/api/integration/merchants"
response=$(curl -s -X POST -H "Content-Type: application/json" -d "$merchant" "$url")
merchantId=$(echo "$response" | grep -o '"id":[0-9]*' | cut -d: -f2 | head -1)
echo -e "\033[0;34mID marchand:\033[0m \033[0;32m$merchantId\033[0m"
applicationIntegrationDto='{
  "name": "cookie factory application",
  "email": "cookie.factory@gmail.com",
  "url": "http://cookie.com",
  "description": "cookie factory application",
  "merchantId":  "'"$merchantId"'"
}'
echo -e "\033[0;34mApplication:\033[0m \033[0;32m$applicationIntegrationDto\033[0m"

url="http://localhost:5012/api/integration/applications"
response=$(curl -s -X POST -H "Content-Type: application/json" -d "$applicationIntegrationDto" "$url")

apiKey=$(echo "$response" | grep -o '"apiKey":"[^"]*' | cut -d'"' -f4)
echo -e "\033[0;34mAPI Key:\033[0m \033[0;32m$apiKey\033[0m"

