CREATE TABLE `favorites` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `UUID` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `categoryId` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_b4ouknomrpfl1tuk87igwnige` (`categoryId`),
  KEY `FK_cu86eu5p1dv4ktc3nutjyachn` (`userId`),
  CONSTRAINT `FK_b4ouknomrpfl1tuk87igwnige` FOREIGN KEY (`categoryId`) REFERENCES `categories` (`id`),
  CONSTRAINT `FK_cu86eu5p1dv4ktc3nutjyachn` FOREIGN KEY (`userId`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


