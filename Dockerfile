FROM tomcat:jdk17

WORKDIR /usr/local/tomcat

ARG WAR_FILE=build/libs/*.war

COPY ${WAR_FILE} /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

ENTRYPOINT ["catalina.sh", "run"]
