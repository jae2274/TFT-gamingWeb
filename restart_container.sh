#!/bin/bash

# Stop the running container if it exists
if [ "$(docker ps -q -f name=tft-gaming-web)" ]; then
    docker stop tft-gaming-web
    docker rm tft-gaming-web
fi

# Pull the latest image
docker pull owl2274/tft-gaming-web

# Run the new container
docker run -d -p 80:8080 -e PROFILE=aws -e JASYPT_PASSWORD=$1 --name tft-gaming-web owl2274/tft-gaming-web