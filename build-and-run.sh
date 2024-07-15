#!/bin/bash

set -e

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

cd ${SCRIPT_DIR}/backend
./gradlew build
cd $SCRIPT_DIR
docker compose up -d

echo "PostIT app is running at http://localhost:8091"
echo "You can use Postman collection located in the ${SCRIPT_DIR}/postman directory"
