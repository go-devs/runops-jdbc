#!/usr/bin/env bash

cd "$(dirname "$0")"
java -jar out/artifacts/runops_jdbc_jar/runops-jdbc.jar login "$1"
