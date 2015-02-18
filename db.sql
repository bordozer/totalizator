CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `UUID` varchar(200) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `login` varchar(20) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;