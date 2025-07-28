FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y --no-install-recommends \
    maven libxrender1 libxtst6 libxi6 && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY ./masi-shared .
RUN mvn -N wrapper:wrapper

RUN chmod +x ./mvnw
RUN ./mvnw -Pprod clean package