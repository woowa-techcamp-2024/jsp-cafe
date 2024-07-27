wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.26/bin/apache-tomcat-10.1.26.tar.gz
sudo tar xzvf apache-tomcat-10.1.26.tar.gz -C /opt/tomcat --strip-components=1
sudo useradd -m -d /opt/tomcat -U -s /bin/false tomcat
sudo chown -R tomcat:tomcat /opt/tomcat/ && sudo chmod -R u+x /opt/tomcat/bin
sudo chown -R 755 /opt/tomcat/webapps/