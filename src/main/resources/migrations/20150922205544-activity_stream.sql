CREATE TABLE `activities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `UUID` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `activityEntryId` int(11) NOT NULL,
  `activityStreamEntryType` int(11) DEFAULT NULL,
  `activityTime` datetime DEFAULT NULL,
  `eventJson` varchar(255) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_7f76jrr3lb9i0k82nlcu3djhs` (`userId`),
  CONSTRAINT `FK_7f76jrr3lb9i0k82nlcu3djhs` FOREIGN KEY (`userId`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;