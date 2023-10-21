#!/bin/bash

APP="${PWD##*/}"

# Building docker image
echo "Begin: Building docker image newbank/$APP"
docker build -t "newbank/$APP" .
echo "Done: Building docker image newbank/$APP"
