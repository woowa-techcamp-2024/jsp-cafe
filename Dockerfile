FROM tomcat:10-jdk17

RUN apt-get update && \
    apt-get install -y maven

COPY . /usr/local/tomcat/webapps/myapp

WORKDIR /usr/local/tomcat/webapps/myapp

RUN mvn clean package

RUN mv target/*.war /usr/local/tomcat/webapps/ROOT.war

RUN rm -rf /usr/local/tomcat/webapps/myapp /root/.m2

EXPOSE 8080
