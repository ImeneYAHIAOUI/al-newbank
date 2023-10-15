#!/bin/bash

function compile_dir()  # $1 is the dir to get it
{
    cd $1
    ./build.sh
    cd ..
}

echo "** Compiling all"

compile_dir "customer-care"

compile_dir "payment-processor"

compile_dir "mock-credit-card-network"

compile_dir "payment-gateway"

echo "** Done all"