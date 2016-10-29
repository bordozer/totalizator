CREATE TABLE `pointsCalculationStrategies` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `UUID` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `pointsDelta` int(11) NOT NULL,
  `pointsForBetWithinDelta` int(11) NOT NULL,
  `pointsForMatchScore` int(11) NOT NULL,
  `pointsForMatchWinner` int(11) NOT NULL,
  `strategyName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

ALTER TABLE cups ADD pointsCalculationStrategyId int(11) NOT NULL;

INSERT INTO `pointsCalculationStrategies` (`UUID`, `version`, `pointsDelta`, `pointsForBetWithinDelta`, `pointsForMatchScore`, `pointsForMatchWinner`, `strategyName`) VALUES
  ('5019a03d-68c7-40b8-b81a-b7f222f16c80', 6, 0, 0, 1, 0, 'NBA strategy');

UPDATE `cups` SET cups.`pointsCalculationStrategyId` = 1;

ALTER TABLE cups ADD CONSTRAINT fk_pointsCalculationStrategyId FOREIGN KEY (pointsCalculationStrategyId) REFERENCES pointsCalculationStrategies(id);

