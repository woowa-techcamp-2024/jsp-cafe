JSP Cafe

# 배포 방법
1. `/opt` 폴더 안에 deploy.sh 스크립트 파일 생성
```
#!/bin/bash

# 변수 설정
APP_DIR="/home/ubuntu/jsp-cafe"
TOMCAT_WEBAPPS="/var/lib/tomcat10/webapps"
WAR_FILE="ROOT.war"

# 1. Gradle 빌드
cd $APP_DIR
./gradlew clean build

# 2. WAR 파일 복사 (sudo 필요)
sudo cp $APP_DIR/build/libs/$WAR_FILE $TOMCAT_WEBAPPS/

# 3. 권한 설정 (sudo 필요)
sudo chown tomcat:tomcat $TOMCAT_WEBAPPS/$WAR_FILE
sudo chmod 644 $TOMCAT_WEBAPPS/$WAR_FILE

# 4. 기존 ROOT 폴더 제거 (sudo 필요)
sudo rm -rf $TOMCAT_WEBAPPS/ROOT

```

2. `/etc/systemd/system/multi-user.target.wants/tomcat10.service` 파일에 라인 추가

```angular2html
[Unit]
Description=Apache Tomcat Web Application Container
After=network.target

[Service]
Type=forking

Environment=JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
Environment=CATALINA_PID=/opt/tomcat/temp/tomcat.pid
Environment=CATALINA_HOME=/opt/tomcat
Environment=CATALINA_BASE=/opt/tomcat

ExecStartPre=/opt/deploy_app.sh # 추가!
ExecStart=/opt/tomcat/bin/startup.sh
ExecStop=/opt/tomcat/bin/shutdown.sh

User=tomcat
Group=tomcat

[Install]
WantedBy=multi-user.target
```

3. 배포스크립트 실행 권한 부여
```angular2html
sudo chmod +x /opt/deploy_app.sh
```

4. 실행
```angular2html
sudo systemctl daemon-reload
sudo systemctl restart tomcat
```
