FROM gradle:jdk17 as builder

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle clean war

FROM tomcat:10.1.26-jre17-temurin-jammy

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=builder /home/gradle/src/build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

# Expose the port Tomcat listens on
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
