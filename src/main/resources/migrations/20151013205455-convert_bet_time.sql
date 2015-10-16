ALTER TABLE matchBets DROP COLUMN betTime;
ALTER TABLE cupTeamBets DROP COLUMN betTime;
ALTER TABLE userGroups DROP COLUMN creationTime;

ALTER TABLE matchBets ADD betTime DATETIME;

TRUNCATE TABLE activities;