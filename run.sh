#!/bin/bash

DIR="$(cd "$(dirname "$0")" && pwd)"

java -jar "$DIR/jasper-converter-1.0.0.jar" &

sleep 3

open http://localhost:8080
