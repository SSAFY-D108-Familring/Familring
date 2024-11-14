## 사용하고자 하는 기본 이미지 설정 (OpenJDK 17 사용)
#FROM openjdk:17-slim
#
## 애플리케이션 jar 파일 위치 설정
#ARG JAR_FILE=build/libs/*.jar
#
## 컨테이너에 jar 파일을 복사
#COPY ${JAR_FILE} timeCapsuleService.jar
#
#EXPOSE 8092
#
## 애플리케이션 실행 명령어 설정
#ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "./timeCapsuleService.jar"]

# 1단계: 빌드 환경 설정
FROM openjdk:17-slim AS builder

# 소스 파일과 필요한 파일을 복사
WORKDIR /app
COPY . .

# Gradle 빌드 수행
RUN ./gradlew build --build-cache -x test

# 2단계: 실제 실행 이미지
FROM openjdk:17-slim

# 빌드 결과물만 복사
COPY --from=builder /app/build/libs/*.jar timeCapsuleService.jar

EXPOSE 8092

# 애플리케이션 실행 명령어 설정
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "./timeCapsuleService.jar"]
