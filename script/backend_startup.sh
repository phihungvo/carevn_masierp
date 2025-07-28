#!/bin/bash

# Set the profile to the first argument, or 'dev' if no argument is provided
PROFILE=${1:-dev}

#create pgdata directory if not exists
if [ ! -d "../pgdata" ]; then
  mkdir ../pgdata
fi
if [ ! -d "../masisale-upload" ]; then
  mkdir ../masisale-upload
fi

# build masi-shared library
cd ../masi-shared
./mvnw clean install
./mvnw package

# navigate to docker-compose and stop all containers
cd ../docker-compose
docker-compose down

# navigate to masi-api directory
cd ../masi-api
npm run cleanup
# run Maven command with the specified or default profile
./mvnw clean package -P$PROFILE jib:dockerBuild -DskipTests=true

# navigate to masi-app/employee directory
cd ../masi-app/employee

# run Maven command with the specified or default profile
./mvnw clean package -P$PROFILE jib:dockerBuild -DskipTests=true

# navigate to masi-app/production directory
cd ../production

# run Maven command with the specified or default profile
./mvnw clean package -P$PROFILE jib:dockerBuild -DskipTests=true

cd ../sale

./mvnw clean package -P$PROFILE jib:dockerBuild -DskipTests=true

cd ../utility
if [ ! -d "uploaded-files" ]; then
  mkdir uploaded-files
fi
chmod 777 ./mvnw

./mvnw clean package -P$PROFILE jib:dockerBuild -DskipTests=true


cd ../logistics
if [ ! -d "uploaded-files" ]; then
  mkdir uploaded-files
fi
chmod 777 ./mvnw

./mvnw clean package -P$PROFILE jib:dockerBuild -DskipTests=true

# navigate to docker-compose directory
cd ../../docker-compose



# run docker-compose up with detached mode
docker-compose up -d

#print success message
echo "MASI services started successfully!!!"