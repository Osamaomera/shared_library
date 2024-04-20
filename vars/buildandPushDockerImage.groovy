#!usr/bin/env groovy
def call(String dockerHubCredentialsID, String imageName) {

	// Log in to DockerHub 
	withCredentials([usernamePassword(credentialsId: "${dockerHubCredentialsID}", usernameVariable: 'USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
		sh 'docker login -u ${USERNAME} -p ${DOCKERHUB_PASSWORD}'
        }
        
        // Build and push Docker image
        echo "Building and Pushing Docker image..."
        sh "docker build -t ${imageName}:${BUILD_NUMBER} ."
        sh "docker push ${imageName}:${BUILD_NUMBER}"	 
}
