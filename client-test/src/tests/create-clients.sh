#!/bin/bash
# création compte client

# Ensure the CSV file is created and has headers

create_account(){
    url="http://localhost:5003/api/costumer"
    data='{
      "firstName": "valentin",
      "lastName": "doe",
      "email": "valentin.doe'$i'@gmail.com",
      "phoneNumber": "0667995895",
      "BirthDate": "1990-01-01",
      "FiscalCountry": "France",
      "address": "123 Main Street"
    }'

    response=$(curl -s -H "Content-Type: application/json" -d "$data" "$url")

    # Extract ID from the response
    id=$(echo "$response" | grep -o '"id":[0-9]*' | cut -d: -f2 | head -1)

    if [ "$?" -eq 0 ]; then
        id=$(echo "$response" | grep -o '"id":[0-9]*' | cut -d: -f2 | head -1)
        echo -e "\033[0;34mID client:\033[0m \033[0;32m$id\033[0m"
        debitCardUrl="http://localhost:5003/api/costumer/$id/virtualCard/credit"
        debitCardResponse=$(curl -s -X POST "$debitCardUrl")
        cardNumber=$(echo "$debitCardResponse" | grep -oi '"cardNumber":"[^"]*' | cut -d'"' -f4)
        cvv=$(echo "$debitCardResponse" | grep -oi '"cvv":"[^"]*' | cut -d'"' -f4)
        expiryDate=$(echo "$debitCardResponse" | grep -oi '"expiryDate":"[^"]*' | cut -d'"' -f4)
        operation='{
          "amount": 1000,
          "operation": "deposit"
        }'
        fundsUrl="http://localhost:5003/api/costumer/${id}/funds"
        response=$(curl -s -o /dev/null -w "%{http_code}" -X PUT -H "Content-Type: application/json" -d "$operation" "$fundsUrl")

        # Append card information to the CSV file
        echo "$id,$cardNumber,$cvv,$expiryDate" >> client_cards.csv

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
}

echo "ClientID,CardNumber,CVV,ExpiryDate" > client_cards.csv

for i in {1..100}
do
  for ((j=1; j<=20; j++))
  do
    create_account
  done 
  sleep 1.5
done

