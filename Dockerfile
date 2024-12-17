
# 1. Java 21 JRE 이미지 사용
FROM amazoncorretto:21-alpine

# 2. 빌드 시 사용할 변수 설정 (JAR 파일 위치)
ARG JAR_FILE=build/libs/*.jar

# 3. 빌드 후 JAR 파일을 Docker 이미지로 복사
COPY ${JAR_FILE} app.jar

# 4. 환경 변수 설정 (optional)
# 프로파일과 환경을 기본값으로 설정할 수 있습니다.
ARG PROFILES=dev
ARG ENV=production

ENV SPRING_PROFILES_ACTIVE=${PROFILES}
ENV SERVER_ENV=${ENV}

# 5. Docker 컨테이너 시작 시 실행할 명령 지정
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-Dserver.env=${SERVER_ENV}", "-jar", "app.jar"]
