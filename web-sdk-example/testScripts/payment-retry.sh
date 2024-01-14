# Assuming ..env is in the same directory as your script
source .env

# Alternatively, you can use the shorthand notation:
# . ..env

# Print the extracted values for verification
cardNumber=$CARD_NUMBER
cvv=$CVV
expiryDate=$EXPIRY_DATE

paymentDto='{
     "cardNumber": "'"${cardNumber}"'",
     "cvv": "'"${cvv}"'",
     "expirationDate": "'"${expiryDate}"'",
     "amount": 500
}'

docker exec -it gateway-db1 redis-cli FLUSHALL > /dev/null
docker exec -it gateway-db2 redis-cli FLUSHALL > /dev/null

urlStatus="http://localhost:3502/api/status/simulate?toggle=false"
response=$(curl -s -X POST "${urlStatus}" -H "Content-Type: application/json" -d '{}')

url1="http://localhost:3501/api/gateway_authorization/simulate?errorCode="
url="http://localhost:3503/api/gateway_authorization/simulate?errorCode="
url2="http://localhost:5001/api/timeout"

ERROR_CODE=200

response=$(curl -s -X POST "${url}${ERROR_CODE}" -H "Content-Type: application/json" -d '{}')
response=$(curl -s -X POST "${url1}${ERROR_CODE}" -H "Content-Type: application/json" -d '{}')
response=$(curl -s -X POST "${url2}" -H "Content-Type: application/json" -d '{}')

curl -s -X POST -H "Content-Type: application/json" -d "$paymentDto" "http://localhost:6906/pay"

sleep 4


url1="http://localhost:3501/api/gateway_authorization/simulate?errorCode="
url="http://localhost:3503/api/gateway_authorization/simulate?errorCode="

ERROR_CODE=500

response=$(curl -s -X POST "${url}${ERROR_CODE}" -H "Content-Type: application/json" -d '{}')
response=$(curl -s -X POST "${url1}${ERROR_CODE}" -H "Content-Type: application/json" -d '{}')

curl -s -X POST -H "Content-Type: application/json" -d "$paymentDto" "http://localhost:6906/pay"

sleep 1

url1="http://localhost:3501/api/gateway_authorization/simulate?errorCode="
url="http://localhost:3503/api/gateway_authorization/simulate?errorCode="

ERROR_CODE=200

esponse=$(curl -s -X POST "${url}${ERROR_CODE}" -H "Content-Type: application/json" -d '{}')
response=$(curl -s -X POST "${url1}${ERROR_CODE}" -H "Content-Type: application/json" -d '{}')

urlStatus="http://localhost:3502/api/status/simulate?toggle=true"
response=$(curl -s -X POST "${urlStatus}" -H "Content-Type: application/json" -d '{}')
