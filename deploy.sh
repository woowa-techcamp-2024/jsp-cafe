# Variables
GRADLE_PROJECT_PATH="$HOME/jsp-cafe"
TOMCAT_WEBAPPS_PATH="/opt/tomcat/webapps"
WAR_NAME="ROOT.war"

# Step 1: Navigate to the Gradle project directory
cd $GRADLE_PROJECT_PATH || { echo "Gradle project path not found!"; exit 1; }

# Step 2: Clean and build the project
./gradlew war || { echo "Gradle build failed!"; exit 1; }

# Step 3: Locate the built WAR file
WAR_FILE_PATH="$GRADLE_PROJECT_PATH/build/libs/cafe-1.0-SNAPSHOT.war"

if [ ! -f "$WAR_FILE_PATH" ]; then
  echo "WAR file not found!"
  exit 1
fi

# Step 4: Remove existing ROOT deployment
sudo rm -rf "$TOMCAT_WEBAPPS_PATH/ROOT" "$TOMCAT_WEBAPPS_PATH/ROOT.war"

# Step 5: Copy the WAR file as ROOT.war to Tomcat's webapps directory
cp "$WAR_FILE_PATH" "$TOMCAT_WEBAPPS_PATH/ROOT.war" || { echo "Failed to copy WAR file!"; exit 1; }

# Step 6: Restart Tomcat
sudo /opt/tomcat/bin/shutdown.sh
sudo /opt/tomcat/bin/startup.sh

echo "Deployment completed successfully!"
