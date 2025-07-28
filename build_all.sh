#!/bin/bash

REPO_DIR=$(pwd)
PACKAGES_DIR=$(pwd)/packages

echo "Repository directory: $REPO_DIR"
echo "Packages directory: $PACKAGES_DIR"

cd $REPO_DIR/masi-api
./mvnw clean verify -Dmaven.repo.local=$PACKAGES_DIR

cd $REPO_DIR/masi-app/employee
./mvnw clean verify -Dmaven.repo.local=$PACKAGES_DIR

cd $REPO_DIR/masi-app/logistics
./mvnw clean verify -Dmaven.repo.local=$PACKAGES_DIR

cd $REPO_DIR/masi-app/production
./mvnw clean verify -Dmaven.repo.local=$PACKAGES_DIR

cd $REPO_DIR/masi-app/sale
./mvnw clean verify -Dmaven.repo.local=$PACKAGES_DIR

cd $REPO_DIR/masi-app/utility
./mvnw clean verify -Dmaven.repo.local=$PACKAGES_DIR

echo "Build packages successfully"
