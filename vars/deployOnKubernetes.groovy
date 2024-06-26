#!/usr/bin/env groovy

// KubernetesCredentialsID 'KubeConfig file'
def call(String k8sCredentialsID, String imageName) {
    
    // Update deployment.yaml with new Docker Hub image
    sh "sed -i 's|image:.*|image: ${imageName}:${BUILD_NUMBER}|g' myapp-deployment.yml"

    // login to k8s Cluster via KubeConfig file
    withCredentials([file(credentialsId: "${k8sCredentialsID}", variable: 'KUBECONFIG_FILE')]) {
        sh 'export KUBECONFIG=${KUBECONFIG_FILE} && oc apply -f myapp-deployment.yml'
    }
}


