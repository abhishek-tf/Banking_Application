# === STAGE 1: Build ===
FROM eclipse-temurin:25-jdk-noble AS builder

WORKDIR /build

COPY BankingApplication/src/ ./src/

RUN mkdir -p out && \
    javac -d out $(find src -name "*.java")

RUN jar --create \
    --file=app.jar \
    --main-class=com.tandf.casestudy.banking.BankApplication \
    -C out .


# === STAGE 2: Runtime ===
FROM eclipse-temurin:25-jre-noble

WORKDIR /app

RUN apt-get update && \
    apt-get install -y ttyd && \
    rm -rf /var/lib/apt/lists/*

COPY --from=builder /build/app.jar app.jar

EXPOSE 10000

CMD ["ttyd","-W","-p","10000","java","-jar","app.jar"]