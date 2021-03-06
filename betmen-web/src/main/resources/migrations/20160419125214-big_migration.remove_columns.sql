ALTER TABLE sportKinds MODIFY COLUMN sportKindName VARCHAR(255) NOT NULL;

ALTER TABLE categories MODIFY COLUMN categoryName VARCHAR(255) NOT NULL;
ALTER TABLE categories MODIFY COLUMN sportKindId INTEGER NOT NULL;

ALTER TABLE pointsCalculationStrategies MODIFY COLUMN strategyName VARCHAR(255) UNIQUE  NOT NULL;

ALTER TABLE teams MODIFY COLUMN categoryId INTEGER NOT NULL;
ALTER TABLE teams MODIFY COLUMN teamName VARCHAR(255) NOT NULL;
ALTER TABLE teams MODIFY COLUMN logoFileName VARCHAR(100);
ALTER TABLE teams MODIFY COLUMN importId VARCHAR(100);

ALTER TABLE cups MODIFY COLUMN cupName VARCHAR(255) NOT NULL;
ALTER TABLE cups MODIFY COLUMN categoryId INTEGER NOT NULL;
ALTER TABLE cups MODIFY COLUMN cupStartTime DATETIME NOT NULL;
ALTER TABLE cups MODIFY COLUMN pointsCalculationStrategyId INTEGER NOT NULL;

ALTER TABLE matches MODIFY COLUMN cupId INTEGER NOT NULL;
ALTER TABLE matches MODIFY COLUMN team1Id INTEGER NOT NULL;
ALTER TABLE matches MODIFY COLUMN team2Id INTEGER NOT NULL;
ALTER TABLE matches MODIFY COLUMN beginningTime DATETIME NOT NULL;

--ALTER TABLE cupWinners ADD UNIQUE INDEX idx_cup_team (cupId, teamId);
--ALTER TABLE cupWinners ADD UNIQUE INDEX idx_cup_position (cupId, cupPosition);
ALTER TABLE cupWinners MODIFY COLUMN cupId INTEGER NOT NULL;
ALTER TABLE cupWinners MODIFY COLUMN teamId INTEGER NOT NULL;
ALTER TABLE cupWinners MODIFY COLUMN cupPosition INTEGER NOT NULL;


ALTER TABLE cupTeams ADD UNIQUE INDEX idx_cupId_teamId (cupId, teamId);


ALTER TABLE activities DROP COLUMN UUID;
ALTER TABLE activities DROP COLUMN version;

ALTER TABLE categories DROP COLUMN UUID;
ALTER TABLE categories DROP COLUMN version;

ALTER TABLE cups DROP COLUMN UUID;
ALTER TABLE cups DROP COLUMN version;

ALTER TABLE cupTeamBets DROP COLUMN UUID;
ALTER TABLE cupTeamBets DROP COLUMN version;

ALTER TABLE cupTeams DROP COLUMN UUID;
ALTER TABLE cupTeams DROP COLUMN version;

ALTER TABLE cupWinners DROP COLUMN UUID;
ALTER TABLE cupWinners DROP COLUMN version;

ALTER TABLE favorites DROP COLUMN UUID;
ALTER TABLE favorites DROP COLUMN version;

ALTER TABLE jobLog DROP COLUMN UUID;
ALTER TABLE jobLog DROP COLUMN version;

ALTER TABLE jobs DROP COLUMN UUID;
ALTER TABLE jobs DROP COLUMN version;

ALTER TABLE matchBets DROP COLUMN UUID;
ALTER TABLE matchBets DROP COLUMN version;

ALTER TABLE matches DROP COLUMN UUID;
ALTER TABLE matches DROP COLUMN version;

ALTER TABLE matchPoints DROP COLUMN UUID;
ALTER TABLE matchPoints DROP COLUMN version;

ALTER TABLE pointsCalculationStrategies DROP COLUMN UUID;
ALTER TABLE pointsCalculationStrategies DROP COLUMN version;

ALTER TABLE sportKinds DROP COLUMN UUID;
ALTER TABLE sportKinds DROP COLUMN version;

ALTER TABLE teams DROP COLUMN UUID;
ALTER TABLE teams DROP COLUMN version;

ALTER TABLE userGroupCups DROP COLUMN UUID;
ALTER TABLE userGroupCups DROP COLUMN version;

ALTER TABLE userGroupMembers DROP COLUMN UUID;
ALTER TABLE userGroupMembers DROP COLUMN version;

ALTER TABLE userGroups DROP COLUMN UUID;
ALTER TABLE userGroups DROP COLUMN version;

ALTER TABLE users DROP COLUMN UUID;
ALTER TABLE users DROP COLUMN version;