#!/bin/sh
docker tag tupperdate-web registry.heroku.com/heig-pdg/web
docker tag tupperdate-api registry.heroku.com/heig-pdg-api/web

# Push heig-pdg image.
echo $TOKEN_HEROKU | docker login registry.heroku.com --username=_ --password-stdin

# Push both container images.
docker push registry.heroku.com/heig-pdg/web
docker push registry.heroku.com/heig-pdg-api/web