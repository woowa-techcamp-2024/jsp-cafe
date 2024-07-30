# Use the official Tomcat image from the Docker Hub
FROM tomcat:10.1.26-jdk17

# Change conf file
## server.xml
RUN rm -rf /usr/local/tomcat/conf/server.xml
COPY tomcat/conf/server.xml /usr/local/tomcat/conf/server.xml

# Change webapps
## default webapp
RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY build/libs/jsp-cafe-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

## manager
COPY tomcat/webapps/manager /usr/local/tomcat/webapps/manager

# Copy MySQL Connector JAR into Tomcat's lib directory
COPY tomcat/lib/mysql-connector-j-8.3.0.jar /usr/local/tomcat/lib/mysql-connector-j-8.3.0.jar

# Expose port 8080 to the outside world
EXPOSE 8080

# Set the entrypoint to run Tomcat
CMD ["catalina.sh", "run"]
