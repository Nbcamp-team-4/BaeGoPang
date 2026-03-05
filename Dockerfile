# 1) Build stage
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Gradle Wrapper 먼저 복사(캐시 효율)
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# 멀티모듈일때
# COPY build.gradle.kts settings.gradle.kts ./

# 소스 복사
COPY src src

# 실행 권한 + 빌드
RUN chmod +x gradlew
RUN ./gradlew clean bootJar -x test

# 2) Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]