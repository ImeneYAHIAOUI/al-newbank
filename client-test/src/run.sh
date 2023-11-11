#!/bin/bash

# Set the number of times to run the command
num_iterations=1000

# Loop to run the command 1000 times asynchronously
for ((i=1; i<=$num_iterations; i++))
do
  echo "Running iteration $i..."
  node main.js >> output.txt &
done

# Wait for all background processes to finish
wait

echo "Script completed."
