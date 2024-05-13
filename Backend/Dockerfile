# Ubuntu 20.04 with OpenJDK 17
FROM ubuntu:20.04

# 필요한 패키지 설치, OpenJDK 17 포함
RUN apt-get update && apt-get install -y openjdk-17-jdk locales && rm -rf /var/lib/apt/lists/*

# 로캘 생성 및 설정
RUN locale-gen ko_KR.UTF-8
ENV LANG ko_KR.UTF-8
ENV LANGUAGE ko_KR:ko
ENV LC_ALL ko_KR.UTF-8

# 환경 설정
ENV JAVA_HOME /usr/lib/jvm/java-17-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH
ENV JAVA_TOOL_OPTIONS "-Dfile.encoding=UTF8"

# 애플리케이션 JAR 파일 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
