sudo zypper in npm

npm install -g bower

bower install

bower install https://github.com/harvesthq/chosen/releases/download/v1.4.0/chosen_v1.4.0.zip

#run from command line
JAVA_HOME=/opt/java/jdk1.8.0_20 ; mvn clean install jetty:run -Djetty.port=9091 -Dspring.profiles.active=test

# in a browser
http://localhost:9091/resources/public/login.html





# IDE IDEA configuration (with JRebel)
jetty:run -Djetty.port=9091 -Dspring.profiles.active=development -Djavaagent:/home/blu/.IntelliJIdea13/config/plugins/jr-ide-idea/lib/jrebel/jrebel.jar



TEMP-------------------------
CREATE TABLE `cupTeams` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `UUID` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `cupId` int(11) DEFAULT NULL,
  `teamId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ktbrihmd7idry22lwoekjb6w` (`cupId`),
  KEY `FK_a11uvstbri6amprixtu0dovkm` (`teamId`),
  CONSTRAINT `FK_a11uvstbri6amprixtu0dovkm` FOREIGN KEY (`teamId`) REFERENCES `teams` (`id`),
  CONSTRAINT `FK_ktbrihmd7idry22lwoekjb6w` FOREIGN KEY (`cupId`) REFERENCES `cups` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;