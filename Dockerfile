# 1. Java 23 JRE 이미지 사용
FROM openjdk:23-jre-slim

# 2. 빌드 시 사용할 변수 설정 (JAR 파일 위치)
ARG JAR_FILE=build/libs/*.jar

# 3. JAR 파일을 Docker 이미지로 복사
COPY ${JAR_FILE} my-sanrio-web-0.0.1-SNAPSHOT.jar

# 4. Docker 컨테이너 시작 시 실행할 명령 지정
ENTRYPOINT ["java", "-jar", "my-sanrio-web-0.0.1-SNAPSHOT.jar"]
