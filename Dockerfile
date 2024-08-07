FROM tomcat:11.0

COPY build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

ENV TZ=UTC

CMD ["catalina.sh", "run"]
