CREATE TABLE `jobs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `UUID` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `jobTaskActive` int(1) DEFAULT NULL,
  `jobName` varchar(255) DEFAULT NULL,
  `jobParametersJSON` TEXT DEFAULT NULL,
  `jobTaskType` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;