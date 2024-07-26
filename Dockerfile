# Use the official Tomcat image from the Docker Hub
FROM tomcat:10.1.26-jdk17

# Remove the default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your WAR file into the Tomcat webapps directory
COPY build/libs/jsp-cafe-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Expose port 8080 to the outside world
EXPOSE 8080

# Set the entrypoint to run Tomcat
CMD ["catalina.sh", "run"]
