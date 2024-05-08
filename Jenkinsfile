pipeline {
    agent any
    environment {
        // 환경 변수 설정
        DOCKER_HUB_USER = 'mine0702'
        DOCKER_HUB_PASS = credentials('DOCKER_HUB_PASS')
        VITE_KAKAO_APP_KEY = credentials('VITE_KAKAO_APP_KEY')
        VITE_SERVICE_URL = credentials('VITE_SERVICE_URL')
        BUILD_ID = credentials('BUILD_ID')
        SONAR_PROJECT_KEY = credentials('SONAR_PROJECT_KEY')
        SONAR_TOKEN = credentials('SONAR_TOKEN')
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Prepare Config') {
            steps {
                dir('Backend') {
                    sh 'chmod -R 775 src/main/resources'
                    withCredentials([file(credentialsId: 'application-db', variable: 'APP_DB_CONFIG')]) {
                        sh 'cp $APP_DB_CONFIG src/main/resources/application-db.yml'
                    }
                    withCredentials([file(credentialsId: 'application-aws', variable: 'AWS_CONFIG')]) {
                        sh 'cp $AWS_CONFIG src/main/resources/application-aws.yml'
                    }
                    withCredentials([file(credentialsId: 'application-filter', variable: 'FILTER_CONFIG')]) {
                        sh 'cp $FILTER_CONFIG src/main/resources/application-filter.yml'
                    }
                    withCredentials([file(credentialsId: 'application-jwt', variable: 'JWT_CONFIG')]) {
                        sh 'cp $JWT_CONFIG src/main/resources/application-jwt.yml'
                    }
                    withCredentials([file(credentialsId: 'application-kakao', variable: 'KAKAO_CONFIG')]) {
                        sh 'cp $KAKAO_CONFIG src/main/resources/application-kakao.yml'
                    }
                    withCredentials([file(credentialsId: 'application-mail', variable: 'MAIL_CONFIG')]) {
                        sh 'cp $MAIL_CONFIG src/main/resources/application-mail.yml'
                    }
                }
            }
        }
        stage('SonarQube analysis') {
            environment {
                scannerHome = tool name: 'SonarQube Scanner'
            }
            steps {
                withSonarQubeEnv('SonarQube') {
                    script {
                        dir('Backend') {
                            sh 'chmod +x ./gradlew' 
                            sh """
                            ./gradlew sonarqube \\
                            -Dsonar.projectKey=S10P31B106_Backend \\
                            -Dsonar.sources=src \\
                            -Dsonar.exclusions=**/build/**,**/test/** \\
                            -Dsonar.host.url=http://15.165.76.184:9000 \\
                            -Dsonar.login=${env.SONAR_TOKEN}
                            """
                        }
                        dir('web-frontend') {
                            sh """
                            ${scannerHome}/bin/sonar-scanner \\
                            -Dsonar.projectKey=S10P31B106_Frontend \\
                            -Dsonar.sources=src \\
                            -Dsonar.exclusions=**/node_modules/**,**/dist/**,**/*.spec.js \\
                            -Dsonar.test.inclusions=**/*.spec.js \\
                            -Dsonar.javascript.lcov.reportPaths=coverage/lcov.info \\
                            -Dsonar.host.url=http://15.165.76.184:9000 \\
                            -Dsonar.login=${env.SONAR_TOKEN}
                            """
                        }
                    }
                }
            }
        }
        stage('Build Docker Images') {
            steps {
                script {
                    // Backend 이미지 빌드
                    dir('Backend') {
                        // Ensure the executable permission for gradlew
                        sh 'chmod +x ./gradlew'
                        sh './gradlew clean bootJar'
                        sh 'docker build -t ${DOCKER_HUB_USER}/${BUILD_ID}-backend .'
                        sh 'docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASS}'
                        sh 'docker push ${DOCKER_HUB_USER}/${BUILD_ID}-backend'
                    }
                    // Frontend 이미지 빌드
                    dir('web-frontend') {
                        sh 'docker build --build-arg VITE_SERVICE_URL=${VITE_SERVICE_URL} --build-arg VITE_KAKAO_APP_KEY=${VITE_KAKAO_APP_KEY} -t ${DOCKER_HUB_USER}/${BUILD_ID}-frontend .'
                        sh 'docker push ${DOCKER_HUB_USER}/${BUILD_ID}-frontend'
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    dir('/var/lib/jenkins/workspace/B106-DOCKER') {
                        sh 'docker-compose -f docker-compose.yml pull && docker-compose -f docker-compose.yml up -d'
                    }
                }
            }
        }
    }
}
