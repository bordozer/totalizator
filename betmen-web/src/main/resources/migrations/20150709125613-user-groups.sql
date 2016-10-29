CREATE TABLE `userGroups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `UUID` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `creationTime` tinyblob,
  `groupName` varchar(100) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_8l4w1rn1g4ve37p0smg235e72` (`userId`),
  CONSTRAINT `FK_8l4w1rn1g4ve37p0smg235e72` FOREIGN KEY (`userId`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;


CREATE TABLE `userGroupMembers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `UUID` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `userGroupId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_q6x91bvlm6fncsh128i2oirx1` (`userId`),
  KEY `FK_j6rm33f3c4b1rky6ti9o1y8n5` (`userGroupId`),
  CONSTRAINT `FK_j6rm33f3c4b1rky6ti9o1y8n5` FOREIGN KEY (`userGroupId`) REFERENCES `userGroups` (`id`),
  CONSTRAINT `FK_q6x91bvlm6fncsh128i2oirx1` FOREIGN KEY (`userId`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;


CREATE TABLE `userGroupCups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `UUID` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `cupId` int(11) DEFAULT NULL,
  `userGroupId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_52dyruf3qe4fv9r9dt5cogs28` (`cupId`),
  KEY `FK_ssq9gclwwwwxicg4tbrb3r9k7` (`userGroupId`),
  CONSTRAINT `FK_ssq9gclwwwwxicg4tbrb3r9k7` FOREIGN KEY (`userGroupId`) REFERENCES `userGroups` (`id`),
  CONSTRAINT `FK_52dyruf3qe4fv9r9dt5cogs28` FOREIGN KEY (`cupId`) REFERENCES `cups` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;