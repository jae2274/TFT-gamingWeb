#!/bin/bash

# Stop the running container if it exists
if [ "$(docker ps -q -f name=tft-gaming-web)" ]; then
    docker stop tft-gaming-web
    docker rm tft-gaming-web
fi

# Pull the latest image
docker pull owl2274/tft-gaming-web

# Run the new container
docker run -d --name tft-gaming-web -p 80:80 owl2274/tft-gaming-web