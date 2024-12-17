# 1. Java 21 JRE 이미지 사용
FROM amazoncorretto:21-alpine

# 2. 빌드 시 사용할 변수 설정 (JAR 파일 위치)
ARG JAR_FILE=target/*.jar

ARG PROFILES
ARG ENV

# 3. JAR 파일을 Docker 이미지로 복사
COPY ${JAR_FILE} app.jar

# 4. Docker 컨테이너 시작 시 실행할 명령 지정
ENTRYPOINT ["java", "-Dspirng.profiles.active=${PROFILES}", "-Dserver.env=${ENV}", "-jar", "app.jar"]
