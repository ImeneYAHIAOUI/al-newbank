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
compile_dir "payment-gateway"
compile_dir "payment-processor"
compile_dir "payment-settlement"
compile_dir "transactions-service"
compile_dir "external-bank"
compile_dir "mock-credit-card-network"

echo "** Done all"

