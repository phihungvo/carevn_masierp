FROM masi-shared AS build

WORKDIR /build

COPY ./masi-app/production/pom.xml .
COPY ./masi-app/production/.mvn ./.mvn
COPY ./masi-app/production/mvnw .

# Copy the shared jar from the shared build stage
RUN cp /app/target/masi-shared-1.0-SNAPSHOT.jar /build
RUN chmod +x ./mvnw
RUN ./mvnw install:install-file -Dfile="/build/masi-shared-1.0-SNAPSHOT.jar" -Dmaven.repo.local=/build/packages
RUN ./mvnw dependency:go-offline -Dmaven.repo.local=/build/packages

COPY ./masi-app/production .
RUN chmod +x ./mvnw
RUN ./mvnw -Pprod clean verify -Dmaven.repo.local=/build/packages


FROM build_runtime AS runtime
COPY --from=build /build/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]