# === STAGE 1: Build the Java Project ===
FROM eclipse-temurin:25-jdk-noble AS builder

WORKDIR /build

COPY BankingApplication/src/ ./src/

RUN mkdir -p out && \
    javac -d out $(find src -name "*.java")

# Create executable JAR
RUN jar --create \
    --file=app.jar \
    --main-class=com.tandf.casestudy.banking.BankApplication \
    -C out .


# === STAGE 2: Runtime Environment ===
FROM eclipse-temurin:25-jre-noble

WORKDIR /app

# Install curl
RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

# Install ttyd
RUN curl -L https://github.com/tsl0922/ttyd/releases/download/1.7.7/ttyd.x86_64 \
    -o /usr/local/bin/ttyd && \
    chmod +x /usr/local/bin/ttyd

# Copy compiled JAR from builder stage
COPY --from=builder /build/app.jar app.jar

# Expose browser terminal port
EXPOSE 10000

# Start Java application through ttyd web terminal
CMD ["ttyd", "-p", "10000", "java", "-jar", "app.jar"]