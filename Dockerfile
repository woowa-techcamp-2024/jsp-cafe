FROM tomcat:10-jdk17

# Install Maven
RUN apt-get update && \
    apt-get install -y maven

# Copy your project files
COPY . /usr/local/tomcat/webapps/myapp

# Set working directory
WORKDIR /usr/local/tomcat/webapps/myapp

# Build the project
RUN mvn clean package

# Move the WAR file to Tomcat webapps directory
RUN mv target/*.war /usr/local/tomcat/webapps/ROOT.war

# Remove source files and Maven local repository to reduce image size
RUN rm -rf /usr/local/tomcat/webapps/myapp /root/.m2

EXPOSE 8080
