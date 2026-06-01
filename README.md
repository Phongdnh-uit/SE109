# Lumina

> A high-performance, observable Real Estate Management API.

Lumina is a robust backend system designed for real estate operations, focusing on scalability, developer efficiency through generic abstractions, and deep operational insight via a modern observability stack.

---

## 🚀 Core Pillars

*   **Security First:** Stateless JWT authentication supplemented by email-based OTP verification.
*   **Developer Efficiency:** Custom Generic CRUD framework reduces boilerplate by 60% while maintaining strict type safety and dynamic filtering via RSQL.
*   **Operational Insight:** Full-stack monitoring with Prometheus, Grafana, and Loki for real-time metrics and log aggregation.
*   **Data Integrity:** Automated database schema evolution via Flyway migrations.

## 🛠 Technical Stack

*   **Runtime:** Java 21
*   **Framework:** Spring Boot 4.0.3
*   **Security:** Spring Security (OAuth2 Resource Server / JWT)
*   **Database:** MySQL 8.x
*   **Migrations:** Flyway
*   **Observability:** Prometheus, Grafana, Loki (via Loki4j)
*   **API Docs:** SpringDoc OpenAPI (Swagger UI)
*   **Quality:** SonarCloud, Spotless (Google Java Format)

## ⚙️ Local Setup

### Prerequisites

*   **JDK 21**
*   **Docker & Docker Compose**
*   **Gradle** (or use the included wrapper `./gradlew`)

### 1. Environment Configuration

Copy the example environment file and fill in your local secrets:

```bash
cp example.env .env
```

### 2. Infrastructure Launch

Start the core database and mail utility:

```bash
docker compose -f docker/local/compose.yaml up -d
```

### 3. Monitoring Stack Launch

Start Prometheus and Grafana for full observability:

```bash
docker compose -f monitoring/compose.yaml up -d
```

### 4. Run the Application

```bash
./gradlew bootRun
```

## 🔗 Operational Access Points

| Service | URL | Credentials |
| :--- | :--- | :--- |
| **API Base** | `http://localhost:8080` | - |
| **Swagger UI** | `http://localhost:8080/swagger-ui/index.html` | - |
| **Prometheus** | `http://localhost:9090` | - |
| **Grafana** | `http://localhost:3000` | `admin` / `secret` |
| **Mailpit** | `http://localhost:8025` | - |

## 🧪 Development & Testing

### Code Quality
Apply formatting and check styles:
```bash
./gradlew spotlessApply
```

### Testing
Run all tests (Integration tests require Docker for Testcontainers):
```bash
./gradlew test
```
