FROM tomcat:10.1.26-jdk17

# 기존 Tomcat의 lib 디렉토리 내용 백업
RUN mv /usr/local/tomcat/lib /usr/local/tomcat/lib_backup

# 프로젝트의 lib 디렉토리 복사
COPY ./build/libs/*.jar /usr/local/tomcat/lib/

# WAR 파일 복사
COPY ./build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

# 작업 디렉토리 설정
WORKDIR /usr/local/tomcat

# Tomcat 실행
CMD ["catalina.sh", "run"]