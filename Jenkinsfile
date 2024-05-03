pipeline {
    agent any
    environment {
        // 'MY_SECRET'는 Jenkins 크리덴셜 ID로, 크리덴셜에서 설정된 비밀 정보를 가져옵니다.
        VITE_KAKAO_APP_KEY = credentials('VITE_KAKAO_APP_KEY')
        SERVICE_URL = credentials('SERVICE_URL')
    }
    stages {
        stage('Example') {
            steps {
                // 환경 변수 사용
                script {
                    echo "VITE_KAKAO_APP_KEY is ${env.SERVICE_URL}"
                    echo "SERVICE_URL is ${env.VITE_KAKAO_APP_KEY}"
                }
            }
        }
    }
}
