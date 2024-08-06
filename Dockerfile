# Base image
FROM tomcat:10.1.0-jdk17

# Maintainer
LABEL maintainer="your_email@example.com"

# Remove default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR file to webapps directory
COPY build/libs/jsp-cafe-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Expose port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
