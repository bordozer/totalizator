ALTER TABLE cups DROP COLUMN cupStartTime;
ALTER TABLE cups CHANGE cupStartTime1 cupStartTime DATETIME;

ALTER TABLE matches DROP COLUMN beginningTime;
ALTER TABLE matches CHANGE beginningTime1 beginningTime DATETIME;