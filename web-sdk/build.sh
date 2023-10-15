#!/bin/bash

APP="${PWD##*/}"

# Building docker image
echo "Begin: Building docker image sdk/$APP"
docker build -t "sdk/$APP" .
echo "Done: Building docker image sdk/$APP"
