#!/bin/bash

function compile_dir()  # $1 is the dir to get it
{
    cd $1
    ./build.sh
    cd ..
}

echo "** Compiling all"

compile_dir "analytics-service"
compile_dir "business-integrator"
compile_dir "customer-care"
compile_dir "fees-calculator"
compile_dir "payment-gateway-authorizer"
compile_dir "payment-gateway-confirmation"
compile_dir "payment-processor"
compile_dir "payment-settlement"
compile_dir "transactions-service"
compile_dir "external-bank"
compile_dir "mock-credit-card-network"
compile_dir "monitoring"
compile_dir "status-reporter"
compile_dir "metrics-service"

echo "** Done all"