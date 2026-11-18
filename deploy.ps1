$ErrorActionPreference = "Stop"

Write-Host "`n====================================="
Write-Host "       AGRICONNECT DEPLOYMENT"
Write-Host "====================================="

# Check kubectl
if (-not (Get-Command kubectl -ErrorAction SilentlyContinue)) {
    throw "kubectl is not installed or not in PATH."
}

$context = kubectl config current-context
if ($LASTEXITCODE -ne 0) {
    throw "No active Kubernetes context found."
}

Write-Host "`nKubernetes context: $context"

$confirm = Read-Host "Deploy AgriConnect to this cluster? (Y/N)"
if ($confirm -notin @("Y", "y")) {
    Write-Host "Deployment cancelled."
    exit 0
}

# Check default storage class
$storage = kubectl get storageclass -o json | ConvertFrom-Json
$defaultStorage = @($storage.items | Where-Object {
    $_.metadata.annotations.'storageclass.kubernetes.io/is-default-class' -eq 'true'
})

if ($defaultStorage.Count -eq 0) {
    throw "No default StorageClass found. Configure persistent storage first."
}

# Validate existing credentials or create them
$existingSecret = kubectl get secret agriconnect-secrets --ignore-not-found -o json

if ($LASTEXITCODE -ne 0) {
    throw "Could not check Kubernetes Secret."
}

if ($existingSecret) {
    $secretData = ($existingSecret | ConvertFrom-Json).data
    $requiredKeys = @("DB_USERNAME", "DB_PASSWORD", "JWT_SECRET")

    foreach ($key in $requiredKeys) {
        if (-not $secretData.PSObject.Properties[$key]) {
            throw "Existing Secret is missing required key: $key"
        }
    }

    Write-Host "Existing credentials validated. Keeping them unchanged."
}
else {
    Write-Host "`nDatabase credentials are required for a new deployment."

    $dbPasswordSecure = Read-Host "Enter database password" -AsSecureString
    $jwtSecretSecure = Read-Host "Enter JWT secret" -AsSecureString

    $dbPtr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($dbPasswordSecure)
    $jwtPtr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($jwtSecretSecure)

    try {
        $dbPassword = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($dbPtr)
        $jwtSecret = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($jwtPtr)

        if ([string]::IsNullOrWhiteSpace($dbPassword) -or
            [string]::IsNullOrWhiteSpace($jwtSecret)) {
            throw "Passwords cannot be empty."
        }

        kubectl create secret generic agriconnect-secrets `
            --from-literal=DB_USERNAME=agriconnect_user `
            "--from-literal=DB_PASSWORD=$dbPassword" `
            "--from-literal=JWT_SECRET=$jwtSecret"

        if ($LASTEXITCODE -ne 0) {
            throw "Could not create Kubernetes Secret."
        }
    }
    finally {
        [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($dbPtr)
        [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($jwtPtr)
        Remove-Variable dbPassword, jwtSecret -ErrorAction SilentlyContinue
    }
}
# Apply configuration and persistent storage
kubectl apply -f .\kubernetes\configmap.yaml
if ($LASTEXITCODE -ne 0) { throw "ConfigMap deployment failed." }

kubectl apply -f .\kubernetes\postgres-pvc.yaml
if ($LASTEXITCODE -ne 0) { throw "Database storage setup failed." }

# Apply services
foreach ($file in @(
    "postgres-service.yaml",
    "backend-service.yaml",
    "weather-service.yaml",
    "frontend-service.yaml"
)) {
    kubectl apply -f ".\kubernetes\$file"
    if ($LASTEXITCODE -ne 0) { throw "Service setup failed: $file" }
}

# Start database and wait
kubectl apply -f .\kubernetes\postgres-deployment.yaml
if ($LASTEXITCODE -ne 0) { throw "PostgreSQL deployment failed." }

kubectl rollout status deployment/postgres --timeout=240s
if ($LASTEXITCODE -ne 0) { throw "PostgreSQL did not become ready." }

# Deploy application components
foreach ($file in @(
    "backend-deployment.yaml",
    "weather-deployment.yaml",
    "frontend-deployment.yaml"
)) {
    kubectl apply -f ".\kubernetes\$file"
    if ($LASTEXITCODE -ne 0) { throw "Deployment failed: $file" }
}

# Verify deployments
foreach ($name in @(
    "agriconnect-backend",
    "agriconnect-weather",
    "agriconnect-frontend"
)) {
    kubectl rollout status "deployment/$name" --timeout=240s
    if ($LASTEXITCODE -ne 0) { throw "Rollout failed: $name" }
}

Write-Host "`n====================================="
Write-Host "    AGRICONNECT DEPLOYMENT READY"
Write-Host "====================================="

kubectl get deployments
kubectl get pods
kubectl get services

Write-Host "`nFor local access, run:"
Write-Host "kubectl port-forward service/frontend-service 8082:80"
Write-Host "Then open http://localhost:8082"
