ARG JAVA_VERSION=21
# -----------------------------------------------------------------------------
# Stage 1: Builder - Build & Extract Layers (Cache Optimized)
# -----------------------------------------------------------------------------
FROM eclipse-temurin:${JAVA_VERSION}-jdk-jammy AS builder
WORKDIR /workspace

# 1. Copy Gradle wrapper & settings (Layer 1: Thay đổi ít nhất)
COPY gradlew .
COPY gradle gradle
COPY settings.gradle.kts .
COPY build.gradle.kts .

# 2. Copy build scripts của các module (Layer 2: Thay đổi trung bình)
RUN ./gradlew dependencies --no-daemon -x test --parallel

COPY src ./src
RUN ./gradlew build --no-daemon -x test --parallel

# -----------------------------------------------------------------------------
# Stage 2: Runtime - Secure & Lightweight
# -----------------------------------------------------------------------------
FROM eclipse-temurin:${JAVA_VERSION}-jre-jammy

WORKDIR /app

# 1. Security: Tạo user non-root để chạy ứng dụng
RUN addgroup --system javauser && adduser --system --shell /bin/false --ingroup javauser javauser

# 2. Copy các lớp JAR đã được tách từ builder stage
COPY --from=builder /workspace/build/libs/*.jar /app/app.jar

# 3. Thiết lập quyền và biến môi trường
USER javauser

ENTRYPOINT ["java","-jar","/app/app.jar"]
