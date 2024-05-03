pipeline {
    agent any
    environment {
        // 환경 변수 설정
        DOCKER_HUB_USER = 'mine0702'
        DOCKER_HUB_PASS = credentials('DOCKER_HUB_PASS')
        VITE_KAKAO_APP_KEY = credentials('VITE_KAKAO_APP_KEY')
        SERVICE_URL = credentials('SERVICE_URL')
        BUILD_ID = credentials('BUILD_ID')
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
                    sh 'pwd'
                    sh 'ls -la'
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
        stage('Build Docker Images') {
            steps {
                script {
                    // Backend 이미지 빌드
                    dir('Backend') {
                        // Gradle을 사용하여 Spring Boot 애플리케이션 빌드
                        sh './gradlew clean bootJar'
                        // Docker 이미지 빌드
                        sh 'docker build -t ${DOCKER_HUB_USER}/${BUILD_ID}-backend .'
                        sh 'docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASS}'
                        sh 'docker push ${DOCKER_HUB_USER}/${BUILD_ID}-backend'
                    }
                    // Frontend 이미지 빌드
                    dir('web-frontend') {
                        sh 'docker build -t ${DOCKER_HUB_USER}/${BUILD_ID}-frontend .'
                        sh 'docker push ${DOCKER_HUB_USER}/${BUILD_ID}-frontend'
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    dir('/home/ubuntu/B106-DOCKER') {
                        // 이미지를 강제로 업데이트하기 위해 pull 실행
                        sh 'docker-compose -f docker-compose.yml pull'
                        // 서비스 시작 또는 업데이트
                        sh 'docker-compose -f docker-compose.yml up -d'
                    }
                }
            }
        }
    }
}