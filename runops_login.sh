#!/usr/bin/env bash

cd "$(dirname "$0")"

if [ -f .env ];
then
    source .env.dev
fi

if [ -z "$1" ]
then
    EMAIL=${EMAIL}
else
    EMAIL=$1
fi
echo $EMAIL

java -jar out/artifacts/runops_jdbc_jar/runops-jdbc.jar login "$EMAIL"
