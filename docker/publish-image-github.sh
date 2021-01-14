#!/bin/sh
docker tag tupperdate-web ghcr.io/heig-pdg/backend

# Log in to GitHub package registry.
echo $TOKEN_GITHUB | docker login ghcr.io -u heig-bot --password-stdin

# Push the Docker images.
docker push ghcr.io/heig-pdg/backend
