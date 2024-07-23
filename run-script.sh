#!/bin/sh

# Gradle 빌드 실행
./gradlew build
source .env

# 기존 Tomcat 서버 중지
TOMCAT_PROCESS=$(lsof -i:8080 -t)
if [ -n "$TOMCAT_PROCESS" ]; then
  kill -9 $TOMCAT_PROCESS
fi

# 기존 배포 파일 및 디렉토리 제거
rm -rf ${TOMCAT_HOME}/webapps/app
rm -rf ${TOMCAT_HOME}/webapps/app.war

# 새로운 war 파일 이동
mv ./build/libs/*.war ${TOMCAT_HOME}/webapps/app.war

# Tomcat 서버 실행
${TOMCAT_HOME}/bin/catalina.sh start
