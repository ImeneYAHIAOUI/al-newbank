# Assuming ..env is in the same directory as your script
source .env

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
     "amount": 1
}'


docker exec -it gateway-db1 redis-cli FLUSHALL > /dev/null
docker exec -it gateway-db2 redis-cli FLUSHALL > /dev/null

url1="http://localhost:3501/api/gateway_authorization/simulate?errorCode="
url="http://localhost:3503/api/gateway_authorization/simulate?errorCode="

ERROR_CODE=200

response=$(curl -s -X POST "${url}${ERROR_CODE}" -H "Content-Type: application/json" -d '{}')
response=$(curl -s -X POST "${url1}${ERROR_CODE}" -H "Content-Type: application/json" -d '{}')


for i in {1..150}
do
    curl -s -X POST -H "Content-Type: application/json" -d "$paymentDto" "http://localhost:6906/pay"
done
