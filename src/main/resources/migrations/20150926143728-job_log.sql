CREATE TABLE `jobLog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `UUID` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `jobTaskId` int(11) NOT NULL,
  `jobTaskType` int(2) NOT NULL,
  `startTime` datetime DEFAULT NULL,
  `finishTime` datetime DEFAULT NULL,
  `jobExecutionState` int(1) DEFAULT NULL,
  `jobParametersJSON` text,
  `jobResultJSON` text DEFAULT NULL,
  `jobTaskInternalTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;