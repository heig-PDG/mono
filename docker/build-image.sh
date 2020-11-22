#!/bin/bash

# TODO : use pushd and popd

# Build server with Gradle.
echo "Building web"
cd ../src
./gradlew web:installDist

# Build Docker image.
cd ../docker
cp -r ../src/web/build/install/web ./image/
docker build -t tupperdate-web ./image/web

# Build OpenAPI with Swagger.
cp ../spec/openapi.yaml ./image/api
docker build -t tupperdate-api ./image/api