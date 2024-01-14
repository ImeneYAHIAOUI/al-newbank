#!/bin/bash

# List of test scripts scripts to execute
script_list=("launch.sh" "payment.sh" "payment-retry.sh" "payment-backpressure.sh")

# Function to execute a script and wait for user input
execute_script() {
    local script=$1

    echo "Executing $script..."
    bash "$script"

    read -p "Press 'Enter' to continue to the next script, or 'Q' to quit: " choice
    if [ "$choice" == "Q" ] && [ "$choice" == "q" ]; then
        echo "Quitting..."
        exit 0
    fi
}

for script in "${script_list[@]}"; do
    execute_script "$script"
done

pid=$(netstat -tulpn | grep 6906 | awk '{print $7}' | awk -F'/' '{print $1}')
kill -9 "$pid" > /dev/null


echo "All tests executed successfully."
