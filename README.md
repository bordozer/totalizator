sudo zypper in npm

npm install -g bower

bower install

JAVA_HOME=/opt/java/jdk1.8.0_20 ; mvn clean install tomcat7:run-war -Dspring.profiles.active=development


jetty:run -Djetty.port=9091 -Dspring.profiles.active=development