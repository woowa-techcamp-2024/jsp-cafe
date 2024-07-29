#!/bin/sh
echo "Deploying the application"\

# Build the application
./gradlew clean build -x test
source .env

# Build the docker image
docker build --no-cache -t "${DOCKER_IMAGE_NAME}" .

# Push the docker image to the docker hub
docker push "${DOCKER_IMAGE_NAME}"