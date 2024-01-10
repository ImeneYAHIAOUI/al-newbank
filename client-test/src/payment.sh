# Assuming .env is in the same directory as your script
source .env

# Alternatively, you can use the shorthand notation:
# . .env

# Extract values from environment variables
cardNumber=$NEWBANK_CARD_NUMBER
cvv=$NEWBANK_CVV
expiryDate=$NEWBANK_EXPIRY_DATE

# Print the extracted values for verification
echo "Card Number: $cardNumber"
echo "CVV: $cvv"
echo "Expiry Date: $expiryDate"

paymentDto='{
     "cardNumber": "'"${cardNumber}"'",
     "cvv": "'"${cvv}"'",
     "expirationDate": "'"${expiryDate}"'",
     "amount": 500
}'

curl -s -X POST -H "Content-Type: application/json" -d "$paymentDto" "http://localhost:6906/payment"
