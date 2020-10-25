pipeline {
    agent any

    stages {

       stage('Git Clone') {
          steps {
                checkout scm
            }
        }

       stage('Get last git commit') {
          steps {
                script {
                git_hash = sh(returnStdout: true, script: "git rev-parse HEAD").trim()
               }
            }
        }

        stage('Build JAR') {
            steps {
                sh "mvn clean install"
            }
        }

        stage('Build Image') {
            steps {
                sh "docker build -t ${NOTIFIER_IMAGE}:${git_hash} -t ${NOTIFIER_IMAGE}:latest ."
            }
        }

        stage('Push Image') {
            steps {
                sh "docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}; docker push ${NOTIFIER_IMAGE}:${git_hash}"
            }
        }

    }
}