#!/bin/bash

for dir in */; do
    if [ -d "$dir" ]; then
        echo "Running Maven build in $dir"
        (cd "$dir" && mvn clean install -DskipTests)
    fi
done
