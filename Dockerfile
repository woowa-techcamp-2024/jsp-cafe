FROM tomcat:10.1.26-jdk17

# Gradle 설치
RUN apt-get update && apt-get install -y wget unzip \
    && wget https://services.gradle.org/distributions/gradle-7.6-bin.zip -P /tmp \
    && unzip -d /opt/gradle /tmp/gradle-7.6-bin.zip \
    && ln -s /opt/gradle/gradle-7.6 /opt/gradle/latest

ENV GRADLE_HOME /opt/gradle/latest
ENV PATH $GRADLE_HOME/bin:$PATH

# 애플리케이션 소스 복사
COPY . /tmp/app
# 필요한 JAR 파일을 Tomcat lib 디렉토리로 복사
COPY --from=build /root/.gradle/caches/modules-2/files-2.1/jakarta.servlet.jsp.jstl/jakarta.servlet.jsp.jstl-api/3.0.0/*.jar /usr/local/tomcat/lib/
COPY --from=build /root/.gradle/caches/modules-2/files-2.1/org.glassfish.web/jakarta.servlet.jsp.jstl/3.0.0/*.jar /usr/local/tomcat/lib/

# Gradle을 사용하여 WAR 파일 빌드 및 JSP 프리컴파일
WORKDIR /tmp/app
RUN gradle clean build

# 빌드된 WAR 파일을 Tomcat webapps 디렉토리로 복사
RUN cp /tmp/app/build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

# Tomcat 설정: JSP 컴파일 관련 옵션 추가
RUN echo "org.apache.jasper.compiler.disablejsr199=true" >> /usr/local/tomcat/conf/catalina.properties

# 작업 디렉토리 설정
WORKDIR /usr/local/tomcat

# Tomcat 실행
CMD ["catalina.sh", "run"]
