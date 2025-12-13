#!/bin/bash

# Build and run the Course Recovery System

echo "Building Course Recovery System..."
mvn clean package -DskipTests

if [ $? -eq 0 ]; then
    echo ""
    echo "Build successful! Starting application..."
    echo ""
    java -jar target/CRS-1.0-SNAPSHOT.jar
else
    echo ""
    echo "Build failed. Please check the error messages above."
    exit 1
fi
