FROM tomcat:latest
COPY ./build/libs/*.war /usr/local/tomcat/webapps/ROOT.war