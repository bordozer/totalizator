JAVA_HOME=/opt/java/jdk1.8.0_20 ; \
MAVEN_OPTS="-javaagent:/home/blu/.IntelliJIdea13/config/plugins/jr-ide-idea/lib/jrebel/jrebel.jar -Xdebug $MAVEN_OPTS" \
mvnDebug clean install tomcat7:run-war -Dspring.profiles.active=development
