# Jenkins Pipeline Shared Library

> This document provides an overview of the Jenkins pipeline Share library for building and deploying Dockerized applications to Kubernetes cluster.


## Pipeline shared library Overview

In vars folder there are two function which are follow these stages to build, push, and deploy Docker images to dockerhub and a Kubernetes cluster:

1. **buildandPushDockerImage.groovy:** Build and push image to DockerHub.

2. **deployOnKubernetes.groovy:** Update kubernetes deployment with the new image versions and deploy it to kubernetes cluster.

## Pipeline shared library functions

### buildandPushDockerImage.groovy:

```
#!usr/bin/env groovy
def call(String dockerHubCredentialsID, String imageName) {

	// Log in to DockerHub 
	withCredentials([usernamePassword(credentialsId: "${dockerHubCredentialsID}", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
		sh 'docker login -u ${USERNAME} -p ${PASSWORD}'
        }
        
        // Build and push Docker image
        echo "Building and Pushing Docker image..."
        sh "docker build -t ${imageName}:${BUILD_NUMBER} ."
        sh "docker push ${imageName}:${BUILD_NUMBER}"	 
}
```

### deployOnKubernetes.groovy:

```
#!/usr/bin/env groovy

// KubernetesCredentialsID 'KubeConfig file'
def call(String k8sCredentialsID, String imageName) {
    
    // Update deployment.yaml with new Docker Hub image
    sh "sed -i 's|image:.*|image: ${imageName}:${BUILD_NUMBER}|g' myapp-deployment.yml"

    // login to k8s Cluster via KubeConfig file
    withCredentials([file(credentialsId: "${k8sCredentialsID}", variable: 'KUBECONFIG_FILE')]) {
        sh "export KUBECONFIG=${KUBECONFIG_FILE} && kubectl apply -f myapp-deployment.yml"
    }
}
```
