ALTER TABLE categories ADD importId varchar(100) DEFAULT NULL;

ALTER TABLE categories ADD remoteGameImportStrategyTypeId int(11) DEFAULT NULL;
UPDATE categories SET remoteGameImportStrategyTypeId = 0;

ALTER TABLE teams ADD importId varchar(100) DEFAULT NULL;