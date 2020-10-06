#!/bin/sh
docker tag tupperdate-web docker.pkg.github.com/heig-pdg/mono/backend

# Log in to GitHub package registry.
echo $TOKEN_GITHUB | docker login https://docker.pkg.github.com -u heig-bot --password-stdin

# Push the Docker images.
docker push docker.pkg.github.com/heig-pdg/mono/backend