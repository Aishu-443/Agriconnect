node {
    def backendImage = 'agriconnect-backend'
    def weatherImage = 'agriconnect-weather'
    def buildTag = env.BUILD_NUMBER ?: 'latest'
    
    try {
        stage('Checkout') { checkout scm }
        stage('Build Backend') { dir('agriconnect-backend') { sh 'mvn clean package -DskipTests' } }
        stage('Test Backend') { dir('agriconnect-backend') { sh 'mvn test' } }
        stage('Test Weather') { dir('weather-service') { sh 'pip install -r requirements.txt && pytest test_app.py -v' } }
        stage('Docker Build') {
            dir('agriconnect-backend') { sh "docker build -t ${backendImage}:latest ." }
            dir('weather-service') { sh "docker build -t ${weatherImage}:latest ." }
        }
        stage('K8s Deploy') {
            sh 'kubectl apply -f kubernetes/'
        }
        stage('Verify') {
            sh 'kubectl rollout status deployment/agriconnect-backend --timeout=120s'
            sh 'kubectl rollout status deployment/agriconnect-weather --timeout=60s'
        }
        currentBuild.result = 'SUCCESS'
    } catch (Exception e) {
        currentBuild.result = 'FAILURE'
        echo "Pipeline failed: ${e.message}"
        throw e
    } finally {
        echo "Build ${buildTag} complete. Result: ${currentBuild.result}"
        cleanWs()
    }
}
