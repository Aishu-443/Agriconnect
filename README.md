# 🌱 AgriConnect: Smart Agriculture & Farmer Support Platform

AgriConnect is an enterprise-grade smart agriculture platform designed to empower farmers, agricultural cooperatives, and agronomy experts. It delivers intelligent farm management, crop health monitoring, real-time weather analytics, marketplace connectivity, and government scheme advisories.

Built with a modern microservices architecture and backed by an industry-standard DevOps delivery pipeline, AgriConnect integrates a Spring Boot backend, a Python Flask weather microservice, PostgreSQL persistent storage, Docker containerization, Kubernetes orchestration, and automated Jenkins CI/CD pipelines.

---

## 📑 Table of Contents
1. [Project Overview](#-project-overview)
2. [Tech Stack](#-tech-stack)
3. [Prerequisites](#-prerequisites)
4. [Local Development Setup](#-local-development-setup)
5. [Default Admin Credentials](#-default-admin-credentials)
6. [Running Tests](#-running-tests)
7. [Docker & Containerized Setup](#-docker--containerized-setup)
8. [Kubernetes Deployment](#-kubernetes-deployment)
9. [Jenkins CI/CD Pipelines](#-jenkins-cicd-pipelines)
10. [API Endpoints Reference](#-api-endpoints-reference)
11. [DevOps Course Modules Coverage](#-devops-course-modules-coverage)
12. [Project Directory Structure](#-project-directory-structure)

---

## 🌿 Project Overview

AgriConnect bridges the gap between digital technology and practical agriculture. Key capabilities include:
- **Real-Time Weather & Forecasting**: Integration with the open Open-Meteo API to provide hyper-local weather alerts, wind speed, precipitation, and 7-day agricultural forecasts without requiring third-party API keys.
- **Crop & Field Advisory**: Algorithmic recommendations for irrigation schedules, fertilizer usage, and harvest timing based on microclimate variables.
- **Enterprise Security**: JWT-based stateless authentication, role-based access control (Admin, Expert, Farmer), and encrypted credential management.
- **Resilient Cloud Architecture**: Fully containerized and Kubernetes-ready, supporting automated self-healing, rolling deployments, and horizontal pod scaling.

---

## 🛠 Tech Stack

| Domain | Technology | Version | Purpose |
| :--- | :--- | :--- | :--- |
| **Backend Core** | Java / Spring Boot | Java 21, Spring Boot 3.x | Primary business logic, REST APIs, JPA/Hibernate ORM |
| **Weather Microservice** | Python / Flask | Python 3.11+, Flask 3.0.3 | Weather data ingestion, Open-Meteo translation, CORS handling |
| **Database** | PostgreSQL | 16.x | Relational database for users, crops, sensor logs, and orders |
| **Build & Dependency** | Apache Maven / pip | Maven 3.9+, pip 24+ | Java compilation, packaging, and Python dependency management |
| **Containerization** | Docker & Docker Compose | Docker Engine 24+, Compose v2 | Multi-container microservice packaging and local orchestration |
| **Container Orchestration** | Kubernetes | 1.28+ (Minikube / Kind / K8s) | Declarative deployments, ConfigMaps, Secrets, PVC, NodePort services |
| **CI/CD Automation** | Jenkins | 2.400+ | Declarative and Scripted automated build, test, package, and deploy pipelines |
| **Testing** | JUnit 5 & pytest | JUnit 5, pytest 8.2+ | Unit, integration, and API contract test automation |
| **Demo & Legacy Integration** | Apache Tomcat | 9.x / 10.x | WAR packaging demonstration and Java Servlet 4.0 mapping |

---

## 📋 Prerequisites

Before setting up AgriConnect locally, verify that the following runtime environments and tools are installed:

- **Java Development Kit (JDK)**: OpenJDK 21 or Eclipse Temurin 21 (`java -version`)
- **Apache Maven**: Version 3.9.0 or higher (`mvn -version`)
- **Python**: Version 3.11.x or higher (`python --version`)
- **PostgreSQL**: Version 16.x server (`psql --version`)
- **Docker**: Docker Engine 24.0+ and Docker Compose v2 (`docker version`, `docker-compose version`)
- **Kubernetes CLI**: `kubectl` connected to a local cluster (Minikube, Kind, Docker Desktop, or K3s)
- **Jenkins**: Local or remote Jenkins server with Git, Docker, Pipeline, and Kubernetes CLI plugins installed

---

## 💻 Local Development Setup

### 1. PostgreSQL Database Configuration

Launch PostgreSQL and execute the following SQL commands to create the dedicated database user and schema:

```sql
-- Connect as postgres superuser
psql -U postgres

-- Create dedicated database user
CREATE USER agriconnect_user WITH PASSWORD 'AgriConnect@2024';

-- Create database owned by agriconnect_user
CREATE DATABASE agriconnect_db OWNER agriconnect_user;

-- Grant required permissions
GRANT ALL PRIVILEGES ON DATABASE agriconnect_db TO agriconnect_user;
```

### 2. Run the Spring Boot Backend

```bash
cd agriconnect-backend
mvn clean spring-boot:run
```
The backend server will launch on `http://localhost:8080`.

### 3. Run the Python Weather Microservice

```bash
cd weather-service

# Create and activate virtual environment (Linux/macOS)
python3 -m venv venv
source venv/bin/activate

# For Windows PowerShell
# python -m venv venv
# .\venv\Scripts\Activate.ps1

# Install required dependencies
pip install -r requirements.txt

# Start the Flask microservice
python app.py
```
The weather service will launch on `http://localhost:5000`.

### 4. Open the Web Frontend

Simply open `frontend/index.html` in any modern web browser or serve it with a lightweight static server:
```bash
# Optional static server
npx serve frontend
# or
python -m http.server 3000 --directory frontend
```

---

## 🔐 Default Admin Credentials

Upon initial database migration and startup, the platform provides default administrative access:

- **Email**: `admin@agriconnect.com`
- **Password**: `Admin@123`
- **Role**: `ROLE_ADMIN`

> [!NOTE]
> For production environments, remember to immediately rotate the administrative password and update `JWT_SECRET` in `kubernetes/secrets.yaml` or your secrets store.

---

## 🧪 Running Tests

AgriConnect includes automated unit and integration tests across both microservices.

### Backend Tests (Spring Boot / JUnit 5)
```bash
cd agriconnect-backend
mvn clean test
```
Test results and Surefire XML reports will be generated in `agriconnect-backend/target/surefire-reports/`.

### Weather Service Tests (Python / pytest)
```bash
cd weather-service
pytest -v
```
To generate JUnit-compatible test reports for Jenkins:
```bash
pytest -v --junitxml=test-results.xml
```

---

## 🐳 Docker & Containerized Setup

Run the entire AgriConnect platform (Database, Spring Boot Backend, and Python Weather Microservice) with a single command:

```bash
# Build and start all services in the background
docker-compose up --build -d

# Check running containers
docker-compose ps

# View unified application logs
docker-compose logs -f

# Stop and clean up containers and volumes
docker-compose down -v
```

### Container Endpoints:
- **PostgreSQL**: `localhost:5432`
- **Spring Boot Backend**: `http://localhost:8080`
- **Weather Microservice**: `http://localhost:5000`

---

## ☸️ Kubernetes Deployment

AgriConnect provides declarative, production-grade Kubernetes manifests located in `kubernetes/`.

### 1. Deploy All Resources
```bash
kubectl apply -f kubernetes/
```

### 2. Verify Deployments & Pods
```bash
# Check deployment status
kubectl get deployments

# Check pods
kubectl get pods -l 'app in (postgres, agriconnect-backend, agriconnect-weather)'

# Check services
kubectl get svc
```

### 3. Service Access (NodePort)
- **Backend Service**: `http://<node-ip>:30080` (or `http://localhost:30080` on Docker Desktop / Minikube tunnel)
- **Weather Service**: `http://<node-ip>:30500` (or `http://localhost:30500` on Docker Desktop / Minikube tunnel)

### 4. Verification Commands
```bash
# Verify rollout status
kubectl rollout status deployment/postgres --timeout=120s
kubectl rollout status deployment/agriconnect-backend --timeout=180s
kubectl rollout status deployment/agriconnect-weather --timeout=120s
```

---

## 🔄 Jenkins CI/CD Pipelines

AgriConnect includes both Declarative and Scripted Jenkins pipeline definitions under `jenkins/`.

### Declarative Pipeline (`jenkins/Jenkinsfile`)
Automates the complete continuous integration and continuous deployment lifecycle:
1. **Checkout**: Pulls latest revision from Git repository.
2. **Build Backend**: Builds Spring Boot executable JAR (`mvn clean package -DskipTests`).
3. **Test Backend**: Runs Maven JUnit test suite and publishes test reports.
4. **Build Flask**: Provisions Python virtualenv, installs requirements, and runs `pytest`.
5. **Docker Build Backend**: Containers packaged with Temurin 21 JRE runtime.
6. **Docker Build Weather**: Packages lightweight Python 3.11 slim image.
7. **Deploy to Kubernetes**: Applies manifests using `kubectl apply -f kubernetes/`.
8. **Verify Deployment**: Confirms zero-downtime rollout completion.
9. **Post**: Automated workspace cleanup and build notifications.

### Scripted Pipeline (`jenkins/scripted-pipeline.groovy`)
Implements the same stages within a Groovy-scripted `node { try { ... } catch { ... } finally { ... } }` construct, featuring parallel test execution and comprehensive error handling.

---

## 📡 API Endpoints Reference

### 🌤 Weather Microservice (`http://localhost:5000`)
| Method | Endpoint | Query Parameters | Description | Status Codes |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/health` | None | Service liveness & readiness health check | 200 |
| `GET` | `/weather/current` | `latitude` (float), `longitude` (float) | Current temperature, humidity, wind speed, weather code and description | 200, 400, 503 |
| `GET` | `/weather/forecast` | `latitude` (float), `longitude` (float) | 7-day daily forecast (temps, precipitation, wind speed, weather codes) | 200, 400, 503 |

#### Sample Weather Current Response:
```json
{
  "location_lat": 17.69,
  "location_lon": 83.22,
  "latitude": 17.69,
  "longitude": 83.22,
  "temperature": 29.5,
  "temperature_2m": 29.5,
  "humidity": 72,
  "relative_humidity_2m": 72,
  "precipitation": 0.0,
  "wind_speed": 14.2,
  "wind_speed_10m": 14.2,
  "weather_code": 1,
  "weather_description": "Mainly clear",
  "source": "Open-Meteo",
  "time": "2026-11-15T12:00"
}
```

### ☕ Spring Boot Backend (`http://localhost:8080`)
| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `GET` | `/actuator/health` | Spring Boot Actuator health status | No |
| `POST` | `/api/auth/register` | Register new user account | No |
| `POST` | `/api/auth/login` | Authenticate user & receive JWT token | No |
| `GET` | `/api/users/profile` | Retrieve authenticated user profile | Yes (Bearer JWT) |
| `GET` | `/api/crops` | List crop varieties & planting guides | Yes (Bearer JWT) |
| `POST` | `/api/crops` | Add new crop record | Yes (Admin/Expert) |
| `GET` | `/api/advisories` | Fetch weather-based agricultural advisories | Yes (Bearer JWT) |
| `GET` | `/api/marketplace/items` | List available agricultural produce & supplies | Yes (Bearer JWT) |

---

## 🎓 DevOps Course Modules Coverage

This repository serves as a practical implementation for DevOps syllabus Modules 1 through 9:

| Module | Topic | AgriConnect Implementation Component |
| :--- | :--- | :--- |
| **Module 1** | DevOps Fundamentals & Linux Administration | Multi-container microservices architecture, bash execution, file permissions, environment variables |
| **Module 2** | Git & GitHub Version Control | Complete `.gitignore`, structured branching, commit history, and `sample.txt` demonstration file |
| **Module 3** | Build Automation with Maven | Spring Boot Maven project, `demo/pom.xml`, dependency scopes, surefire testing, JAR/WAR packaging |
| **Module 4** | Web Servers & Application Deployment | Apache Tomcat deployment demo (`demo/HelloServlet.java`, `web.xml`, `index.html`), Flask demo (`demo/hello.py`) |
| **Module 5** | Continuous Integration with Jenkins | Declarative Pipeline (`jenkins/Jenkinsfile`), Scripted Pipeline (`jenkins/scripted-pipeline.groovy`), test reporting |
| **Module 6** | Containerization with Docker | `agriconnect-backend/Dockerfile`, `weather-service/Dockerfile`, multi-stage builds, root `docker-compose.yml` |
| **Module 7** | Container Orchestration with Kubernetes | Manifests in `kubernetes/`: Pods, Deployments, Services (ClusterIP, NodePort), ConfigMaps, Secrets, PVC |
| **Module 8** | Microservices Architecture & Python Integration | Python Flask microservice (`weather-service/app.py`), Open-Meteo REST client, CORS, pytest mock testing |
| **Module 9** | Production CI/CD & Cloud Delivery | Full automated pipeline: Checkout ➔ Build ➔ Test ➔ Containerize ➔ K8s Rollout ➔ Health Verification |

---

## 🗂 Project Directory Structure

```text
fearless-tesla/
├── .gitignore                               # Comprehensive Git ignore rules
├── README.md                                # Platform documentation and runbook
├── sample.txt                               # DevOps Module 2 Git demo sample file
├── docker-compose.yml                       # Multi-container orchestration (Postgres, Backend, Weather)
│
├── agriconnect-backend/                     # Spring Boot core backend microservice
│   └── Dockerfile                           # Eclipse Temurin 21 JRE Docker image definition
│
├── weather-service/                         # Python Flask weather microservice
│   ├── Dockerfile                           # Python 3.11-slim container image definition
│   ├── app.py                               # Flask 3.x application with Open-Meteo integration
│   ├── requirements.txt                     # Python production & testing dependencies
│   └── test_app.py                          # Pytest suite with mocked HTTP endpoints
│
├── kubernetes/                              # Kubernetes production manifests
│   ├── configmap.yaml                       # Application configuration variables
│   ├── secrets.yaml                         # Encrypted base64 database & JWT credentials
│   ├── postgres-pvc.yaml                    # 5Gi PersistentVolumeClaim for PostgreSQL data
│   ├── postgres-deployment.yaml             # PostgreSQL database deployment
│   ├── postgres-service.yaml                # ClusterIP service on port 5432
│   ├── backend-deployment.yaml              # Spring Boot 2-replica deployment with health probes
│   ├── backend-service.yaml                 # NodePort service exposed on port 30080
│   ├── weather-deployment.yaml              # Flask weather deployment with liveness probes
│   └── weather-service.yaml                 # NodePort service exposed on port 30500
│
├── jenkins/                                 # CI/CD automation pipelines
│   ├── Jenkinsfile                          # Declarative Jenkins pipeline (Checkout -> Build -> Test -> Docker -> K8s)
│   └── scripted-pipeline.groovy             # Scripted Groovy pipeline with try-catch & parallel testing
│
└── demo/                                    # DevOps Module 3 & 4 demonstration modules
    ├── pom.xml                              # Maven WAR project configuration (javax.servlet-api)
    ├── hello.py                             # Lightweight standalone Flask demo
    └── src/
        └── main/
            ├── java/
            │   └── com/
            │       └── demo/
            │           └── HelloServlet.java # HttpServlet implementation responding to /hello
            └── webapp/
                ├── index.html               # Welcome page deployed on Tomcat
                └── WEB-INF/
                    └── web.xml              # Servlet mapping descriptor
```

---

*AgriConnect DevOps Engineering Team © 2026. Built for sustainable smart agriculture.*
