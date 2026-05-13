#!/bin/bash
# ─────────────────────────────────────────────────────────────────
#  Employee Management System — Build & Run Script
#  Usage: bash run.sh
# ─────────────────────────────────────────────────────────────────

set -e

JAR_SQLITE="lib/sqlite-jdbc-3.44.1.0.jar"
JAR_SLF4J_API="lib/slf4j-api-1.7.32.jar"
JAR_SLF4J_NOP="lib/slf4j-nop-1.7.32.jar"

CP_COMPILE="$JAR_SQLITE"
CP_RUN="out:$JAR_SQLITE:$JAR_SLF4J_API:$JAR_SLF4J_NOP"

echo "🔨 Compiling..."
mkdir -p out
javac -cp "$CP_COMPILE" \
      -sourcepath src \
      -d out \
      $(find src -name "*.java")

echo "✅ Compiled successfully."
echo "🚀 Starting Employee Management System..."
echo ""

java -cp "$CP_RUN" Main
