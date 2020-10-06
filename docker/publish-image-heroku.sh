#!/bin/sh
docker tag tupperdate-web registry.heroku.com/heig-pdg/web

# Log in to GitHub package registry.
echo $TOKEN_HEROKU | docker login registry.heroku.com --username=_ --password-stdin

# Push the Docker images.
docker push registry.heroku.com/heig-pdg/web