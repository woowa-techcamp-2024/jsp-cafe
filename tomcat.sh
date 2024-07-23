#!/bin/bash
# 1. tomcat폴더의 shutdown.sh를 실행
echo "Stopping Tomcat..."
TOMCAT_HOME=/Users/woowatech/apache-tomcat-10.1.26
$TOMCAT_HOME/bin/shutdown.sh
# 2. ./gradlew clean build를 통해 ROOT.war 생성
echo "Building the project..."
./gradlew clean build
# 3. tomcat 폴더의 webapps 하위의 ROOT.war 삭제
echo "Deleting old ROOT.war from Tomcat webapps..."
rm -f $TOMCAT_HOME/webapps/ROOT.war
# 4. build/libs의 ROOT.war를 톰캣 하위로 이동
echo "Deploying new ROOT.war to Tomcat..."
cp build/libs/ROOT.war $TOMCAT_HOME/webapps/
# 5. tomcat의 startup.sh를 실행
echo "Starting Tomcat..."
$TOMCAT_HOME/bin/startup.sh
echo "Deployment completed!"