FROM tomcat:11.0

COPY /build/libs/jsp-cafe-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
