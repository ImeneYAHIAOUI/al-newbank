# Assuming ..env is in the same directory as your script
source .env

urlStatus="http://localhost:3502/api/status/simulate?toggle=true"
curl -s -X POST "${urlStatus}" -H "Content-Type: application/json" -d '{}'

# Alternatively, you can use the shorthand notation:
# . ..env

# Extract values from environment variables
cardNumber=$CARD_NUMBER
cvv=$CVV
expiryDate=$EXPIRY_DATE



paymentDto='{
     "cardNumber": "'"${cardNumber}"'",
     "cvv": "'"${cvv}"'",
     "expirationDate": "'"${expiryDate}"'",
     "amount": 100
}'

docker exec -it gateway-db1 redis-cli FLUSHALL > /dev/null
docker exec -it gateway-db2 redis-cli FLUSHALL > /dev/null

curl -s -X POST -H "Content-Type: application/json" -d "$paymentDto" "http://localhost:6906/pay"


urlStatus="http://localhost:3502/api/status/simulate?toggle=true"
curl -s -X POST "${urlStatus}" -H "Content-Type: application/json" -d '{}'