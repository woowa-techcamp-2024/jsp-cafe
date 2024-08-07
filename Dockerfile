FROM tomcat:latest

# 필요한 패키지 설치
RUN apt-get update && apt-get install -y gradle && apt-get install -y bash

# 작업 디렉토리 설정
WORKDIR /app

# 소스 코드 복사
COPY . .

# Gradle Wrapper에 실행 권한 부여
RUN chmod +x ./gradlew

# Gradle 빌드 실행
RUN ./gradlew clean build --info --stacktrace -x test

# 기존 war 삭제
RUN rm -Rf /usr/local/tomcat/webapps/ROOT

# 빌드된 WAR 파일을 Tomcat webapps 디렉토리로 복사
RUN cp build/libs/ROOT.war /usr/local/tomcat/webapps/ROOT.war

ENV DB_HOST=host.docker.internal

# Tomcat 실행
CMD ["catalina.sh", "run"]