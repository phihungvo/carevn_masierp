#!/bin/bash

REPO_DIR=$(pwd)
PACKAGES_DIR=$(pwd)/packages

echo "Repository directory: $REPO_DIR"
echo "Packages directory: $PACKAGES_DIR"

cd $REPO_DIR/masi-shared
./mvnw clean package -Dmaven.repo.local=$PACKAGES_DIR
./mvnw install:install-file -Dfile="$REPO_DIR/masi-shared/target/masi-shared-1.0-SNAPSHOT.jar" -Dmaven.repo.local=$PACKAGES_DIR

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
