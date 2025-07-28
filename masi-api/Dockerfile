FROM masi-shared AS build

WORKDIR /build


# install node  <node.version>v18.18.2</node.version>
# install npm  <npm.version>10.2.4</npm.version>
# RUN apt install -y curl software-properties-common
# RUN curl -fsSL https://deb.nodesource.com/setup_18.x | bash -
# RUN apt-get update
# RUN apt-get install -y nodejs
# RUN npm install -g npm@10.2.4

COPY ./masi-api/pom.xml .
COPY ./masi-api/.mvn ./.mvn
COPY ./masi-api/mvnw .
# Copy the shared jar from the shared build stage
RUN cp /app/target/masi-shared-1.0-SNAPSHOT.jar .
RUN chmod +x ./mvnw
RUN ./mvnw install:install-file -Dfile="/build/masi-shared-1.0-SNAPSHOT.jar" -Dmaven.repo.local=/build/packages
RUN ./mvnw dependency:go-offline -Dmaven.repo.local=/build/packages


COPY ./masi-api .

# RUN npm install
# RUN npm run webapp:prod

RUN ./mvnw -Pprod clean verify -Dmaven.repo.local=/build/packages

FROM build_runtime AS runtime
COPY --from=build /build/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
