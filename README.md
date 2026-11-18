# AgriConnect â€” Smart Agriculture Platform

AgriConnect is a smart agriculture platform that combines a web frontend, Spring Boot backend, PostgreSQL database, and Python-based weather service.

The project demonstrates containerization, CI/CD automation, Docker Hub image publishing, and Kubernetes deployment.

---

## 1. Project Overview

AgriConnect provides a platform for agricultural information and services through a web-based interface.

### Main Components

- **Frontend:** HTML, CSS, JavaScript, and Nginx.
- **Backend:** Java Spring Boot REST API.
- **Database:** PostgreSQL.
- **Weather Service:** Python Flask application.
- **Containerization:** Docker.
- **Image Registry:** Docker Hub.
- **CI/CD:** Jenkins.
- **Orchestration:** Kubernetes.

### Deployment Architecture

```text
                 Developer
                     |
                     v
                  GitHub
                     |
                     v
                  Jenkins
                     |
             Build and Test
                     |
                     v
               Docker Images
                     |
                     v
                 Docker Hub
                     |
                     v
                 Kubernetes
                     |
        +------------+------------+
        |            |            |
        v            v            v
     Frontend     Backend    Weather Service
        |            |            |
        +------------+------------+
                     |
                     v
                 PostgreSQL
```

---

## 2. Technology Stack

| Component | Technology |
|---|---|
| Frontend | HTML, CSS, JavaScript |
| Web Server | Nginx |
| Backend | Java, Spring Boot |
| Database | PostgreSQL 16 |
| Weather Service | Python, Flask |
| Backend Testing | JUnit 5, Maven |
| Weather Testing | pytest |
| Containerization | Docker |
| Image Registry | Docker Hub |
| CI/CD | Jenkins |
| Orchestration | Kubernetes |
| Version Control | Git, GitHub |

---

## 3. Prerequisites

Install the following tools before deploying AgriConnect.

- Git
- Docker Desktop
- Docker Hub account
- Java Development Kit compatible with the backend
- Apache Maven
- Python 3
- Jenkins
- kubectl
- A Kubernetes cluster

For local Kubernetes deployment, Docker Desktop Kubernetes can be used.

For deployment on another computer or cloud environment, configure an appropriate Kubernetes cluster and its storage provisioner.

---

## 4. Get the Project

Clone the GitHub repository:

```powershell
git clone https://github.com/Aishu-443/Agriconnect.git
```

Navigate into the project:

```powershell
cd Agriconnect
```

Check the project files:

```powershell
Get-ChildItem
```

---

## 5. Local Development Setup

### Backend

Navigate to the backend directory:

```powershell
cd agriconnect-backend
```

Build and test the Spring Boot application:

```powershell
mvn clean verify
```

Return to the project root:

```powershell
cd ..
```

### Weather Service

Navigate to the weather service:

```powershell
cd weather-service
```

Create a Python virtual environment:

```powershell
python -m venv venv
```

Activate it:

```powershell
.\venv\Scripts\Activate.ps1
```

Install dependencies:

```powershell
pip install -r requirements.txt
```

Run the tests:

```powershell
pytest -v
```

Return to the project root:

```powershell
deactivate
cd ..
```

### Frontend

The frontend is served using Nginx in the containerized deployment.

For a basic static preview, run:

```powershell
npx serve frontend
```

Alternatively:

```powershell
python -m http.server 3000 --directory frontend
```

---

## 6. Admin Account Configuration

Administrator credentials should be configured securely for each deployment.

- Do not publish default passwords in the repository.
- Configure administrator access using the application's supported account setup process.
- Change any default credentials before exposing the application to other users.
- Never commit passwords, access tokens, or signing secrets to GitHub.

---

## 7. Running Tests

AgriConnect includes automated tests for its backend and weather service.

### Backend Tests

```powershell
cd agriconnect-backend
mvn clean test
```

Test reports are generated under:

```text
agriconnect-backend/target/surefire-reports/
```

### Weather Service Tests

```powershell
cd weather-service
pytest -v
```

To generate a Jenkins-compatible test report:

```powershell
pytest -v --junitxml=test-results.xml
```

---

## 8. Docker and Docker Hub

AgriConnect uses Docker to package its application components into portable container images.

### Docker Hub Repositories

The following public image repositories are used:

| Component | Docker Hub Image |
|---|---|
| Backend | `aishvitha/agriconnect-backend` |
| Frontend | `aishvitha/agriconnect-frontend` |
| Weather | `aishvitha/agriconnect-weather` |

### Pull Images from Docker Hub

```powershell
docker pull aishvitha/agriconnect-backend:latest
docker pull aishvitha/agriconnect-frontend:latest
docker pull aishvitha/agriconnect-weather:latest
```

### Build Images Manually

Run these commands from the project root:

```powershell
docker build -t aishvitha/agriconnect-backend:latest ./agriconnect-backend
docker build -t aishvitha/agriconnect-frontend:latest ./frontend
docker build -t aishvitha/agriconnect-weather:latest ./weather-service
```

### Push Images to Docker Hub

Authenticate using Docker:

```powershell
docker login
```

Push the images:

```powershell
docker push aishvitha/agriconnect-backend:latest
docker push aishvitha/agriconnect-frontend:latest
docker push aishvitha/agriconnect-weather:latest
```

Use a Docker Hub access token for authentication where required. Never store the token in this README or commit it to GitHub.

---

## 9. Docker Compose Deployment

Docker Compose can run the application services together on a machine with Docker installed.

### Step 1: Configure Environment Variables

Create a local environment file:

```powershell
Copy-Item .env.example .env
```

Open it:

```powershell
notepad .env
```

Replace the placeholder values with strong, private credentials.

The `.env` file is excluded from Git.

### Step 2: Validate Docker Compose Configuration

```powershell
docker compose --env-file .env config --quiet
```

### Step 3: Build and Start Services

```powershell
docker compose up --build -d
```

### Step 4: Verify Containers

```powershell
docker compose ps
```

### Step 5: View Logs

```powershell
docker compose logs -f
```

### Step 6: Stop Services

```powershell
docker compose down
```

To remove the database volume as well, use:

```powershell
docker compose down -v
```

**Warning:** Removing the volume deletes the persisted database data. Do not use this command if you need to retain your database.

### Docker Compose Endpoints

| Component | Local Address |
|---|---|
| Frontend | `http://localhost:8081` |
| Backend | `http://localhost:8083` |
| Weather Service | `http://localhost:5000` |
| PostgreSQL | `localhost:5432` |

---

## 10. Kubernetes Deployment

AgriConnect includes Kubernetes manifests in the `kubernetes/` directory.

The deployment script configures the required resources, checks for credentials, deploys PostgreSQL, and then deploys the application components.

### Prerequisites

Verify that Kubernetes is running:

```powershell
kubectl cluster-info
```

Check the active context:

```powershell
kubectl config current-context
```

Check the available nodes:

```powershell
kubectl get nodes
```

### Deploy AgriConnect

From the project root, run:

```powershell
.\deploy.ps1
```

The script checks for an existing Kubernetes Secret. If one does not exist, it prompts for the database password and JWT secret.

Do not share these values or commit them to GitHub.

### Verify Deployments

```powershell
kubectl get deployments
```

```powershell
kubectl get pods
```

```powershell
kubectl get services
```

### Verify Rollouts

```powershell
kubectl rollout status deployment/postgres --timeout=240s
kubectl rollout status deployment/agriconnect-backend --timeout=240s
kubectl rollout status deployment/agriconnect-weather --timeout=240s
kubectl rollout status deployment/agriconnect-frontend --timeout=240s
```

### Access the Frontend

Run:

```powershell
kubectl port-forward service/frontend-service 8082:80
```

Open the following address in a browser:

```text
http://localhost:8082
```

Keep the PowerShell window running while accessing the application.

The frontend Nginx server routes API requests to the backend and weather service inside Kubernetes.

### Kubernetes Services

| Service | Port |
|---|---|
| Frontend | 80 |
| Backend | 8080 |
| Weather | 5000 |
| PostgreSQL | 5432 |

The Kubernetes service configuration uses internal service discovery for communication between application components.

---

## 11. Jenkins CI/CD Pipeline

AgriConnect uses Jenkins to automate the build, test, image publishing, and Kubernetes deployment process.

### Pipeline Configuration

The Jenkins pipeline is defined in:

```text
jenkins/Jenkinsfile
```

Configure a Jenkins Pipeline job with:

- **Repository:** `https://github.com/Aishu-443/Agriconnect.git`
- **Branch:** `main`
- **Script Path:** `jenkins/Jenkinsfile`
- **Pipeline Definition:** Pipeline script from SCM
- **SCM:** Git

### Jenkins Credentials

Configure Docker Hub authentication in Jenkins Credentials.

The credential ID expected by the pipeline is:

```text
dockerhub-creds
```

Use a Docker Hub username and access token with the required permissions.

Never place the access token directly inside the Jenkinsfile.

### Pipeline Stages

The pipeline performs these stages:

1. **Checkout:** Retrieves the source code from GitHub.
2. **Build and Test Backend:** Builds and tests the Spring Boot application using Maven.
3. **Test Weather Service:** Installs Python dependencies and executes pytest.
4. **Build Docker Images:** Builds the backend, frontend, and weather service images.
5. **Push Docker Images:** Publishes versioned images and the latest tags to Docker Hub.
6. **Deploy to Kubernetes:** Updates the application deployments with the newly published image versions.
7. **Verify Deployment:** Checks Kubernetes rollout status and application deployments.

### Build Verification

After running the Jenkins job, inspect:

- Console Output
- Build status
- Test reports
- Docker Hub repositories
- Kubernetes deployment status

A successful pipeline should complete with all required stages successful and the application deployments ready.

### Environment Configuration

The current Jenkinsfile contains Windows-specific executable paths and a local kubeconfig path.

When using another machine, update the executable paths, Jenkins credentials, and Kubernetes access configuration to match that environment.

---

## 12. API Endpoints Reference

The frontend uses Nginx reverse proxying so that browser requests can use the same origin as the frontend.

Example endpoints:

| Endpoint | Purpose |
|---|---|
| `/api/announcements` | Retrieves announcements |
| `/weather/current` | Retrieves current weather information |

Example weather request:

```text
/weather/current?latitude=17.69&longitude=83.22
```

For local Kubernetes access, these endpoints can be tested through:

```text
http://localhost:8082/api/announcements
```

```text
http://localhost:8082/weather/current?latitude=17.69&longitude=83.22
```

---

## 13. Project Directory Structure

```text
Agriconnect/
|
|-- agriconnect-backend/
|   |-- src/
|   |-- pom.xml
|   |-- Dockerfile
|
|-- frontend/
|   |-- index.html
|   |-- css/
|   |-- js/
|   |-- Dockerfile
|   |-- nginx.conf
|
|-- weather-service/
|   |-- app.py
|   |-- requirements.txt
|   |-- Dockerfile
|   |-- test_app.py
|
|-- jenkins/
|   |-- Jenkinsfile
|
|-- kubernetes/
|   |-- backend-deployment.yaml
|   |-- backend-service.yaml
|   |-- configmap.yaml
|   |-- frontend-deployment.yaml
|   |-- frontend-service.yaml
|   |-- postgres-deployment.yaml
|   |-- postgres-pvc.yaml
|   |-- postgres-service.yaml
|   |-- weather-deployment.yaml
|   |-- weather-service.yaml
|
|-- .env.example
|-- .gitignore
|-- docker-compose.yml
|-- deploy.ps1
|-- README.md
```

**Note:** .env is a local sensitive configuration file and must not be committed. Kubernetes credentials are created securely by deploy.ps1 or configured separately for your cluster.

---

## 14. Security Notes

Follow these practices when deploying AgriConnect:

- Keep passwords and signing secrets out of source control.
- Keep `.env` and Kubernetes Secret files excluded from Git.
- Use Jenkins Credentials for Docker Hub authentication.
- Use strong database passwords and JWT signing secrets.
- Avoid publishing administrator passwords in documentation.
- Use HTTPS and appropriate access controls when exposing the application publicly.
- Restrict access to the Kubernetes cluster and its credentials.

Kubernetes Secret values are base64-encoded by default. Base64 encoding is not encryption. Configure appropriate cluster access controls and encryption at rest for production environments.

---

## 15. Troubleshooting

### Docker is not running

Start Docker Desktop and verify:

```powershell
docker version
```

### Kubernetes cluster is unavailable

Check:

```powershell
kubectl cluster-info
kubectl get nodes
```

### Pods are not starting

Inspect:

```powershell
kubectl get pods
kubectl describe pods
```

View application logs:

```powershell
kubectl logs deployment/agriconnect-backend
```

### Deployment rollout fails

Check:

```powershell
kubectl rollout status deployment/agriconnect-backend
kubectl get events --sort-by=.metadata.creationTimestamp
```

### Frontend cannot access backend

Check the Kubernetes services:

```powershell
kubectl get services
```

Check frontend Nginx configuration and the backend and weather service endpoints.

### Jenkins cannot access Docker

Verify Docker Desktop is running and that the executable paths configured in the Jenkinsfile are correct for the machine running Jenkins.

---

## 16. DevOps Concepts Demonstrated

This project demonstrates:

- Version control using Git and GitHub.
- Automated builds and testing.
- CI/CD pipeline implementation using Jenkins.
- Containerization using Docker.
- Docker image publishing using Docker Hub.
- Multi-service application deployment.
- Kubernetes Deployments and Services.
- Persistent storage using PersistentVolumeClaims.
- Application configuration using ConfigMaps.
- Credential management using Kubernetes Secrets and Jenkins Credentials.
- Application health checks and rollout verification.
- Container networking and service discovery.

---

## Deployment Notes

AgriConnect supports multiple deployment approaches:

| Deployment Method | Purpose |
|---|---|
| Local development | Develop and test individual components |
| Docker Compose | Run the application services together |
| Kubernetes | Deploy and manage containerized services |
| Jenkins CI/CD | Automate testing, image publishing, and deployment |

**Important:** Docker Hub stores container images; it does not independently host the running application. Anyone deploying this project on another machine needs Docker Compose or a Kubernetes environment, along with the required configuration and credentials.

---

**AgriConnect DevOps Engineering Team (C) 2026**

Built for sustainable smart agriculture.
