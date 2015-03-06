sudo zypper in npm

npm install -g bower

bower install

JAVA_HOME=/opt/java/jdk1.8.0_20 ; mvn clean install jetty:run -Djetty.port=9091 -Dspring.profiles.active=development

from IDE IDEA
jetty:run -Djetty.port=9091 -Dspring.profiles.active=development -Djavaagent:/home/blu/.IntelliJIdea13/config/plugins/jr-ide-idea/lib/jrebel/jrebel.jar