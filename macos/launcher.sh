#!/bin/bash

# Caminho absoluto da pasta onde o script está
APP_DIR="$(cd "$(dirname "$0")" && pwd)"

# Caminho do JAR (copiado pelo jpackage)
JAR="$APP_DIR/../app/jasper-converter-1.0.0.jar"

# Sobe o backend em background
java -jar "$JAR" &

# Aguarda o Spring Boot subir
sleep 3

# Abre o navegador (macOS)
open http://localhost:8080
