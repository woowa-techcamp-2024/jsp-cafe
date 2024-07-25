FROM tomcat:10.1.26-jre17-temurin-jammy
LABEL authors="KyungMin Lee"

COPY ./build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]