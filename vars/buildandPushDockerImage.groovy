#!usr/bin/env groovy
def call(String dockerHubCredentialsID, String imageName) {

	// Log in to DockerHub 
	withCredentials([usernamePassword(credentialsId: "${dockerHubCredentialsID}", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
		bat 'docker login -u ${USERNAME} -p ${PASSWORD}'
        }
        
        // Build and push Docker image
        echo "Building and Pushing Docker image..."
        bat "docker build -t ${imageName}:${BUILD_NUMBER} ."
        bat "docker push ${imageName}:${BUILD_NUMBER}"	 
}
