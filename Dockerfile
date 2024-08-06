# 빌드 스테이지: Gradle과 JDK 17을 사용하여 WAR 파일 빌드
FROM gradle:7.6.1-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build

# 실행 스테이지: Tomcat 10.1.26과 JDK 17을 사용하여 WAR 실행
FROM tomcat:10.1.26-jdk17
WORKDIR /usr/local/tomcat/webapps/
COPY --from=build /app/build/libs/*.war ./ROOT.war

# 8080 포트 노출 (Tomcat 기본 포트)
EXPOSE 8080

# Tomcat 실행
CMD ["catalina.sh", "run"]
