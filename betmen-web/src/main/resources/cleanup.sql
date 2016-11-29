do not run;

DELETE FROM matchPoints;

DELETE FROM userGroupMembers;
DELETE FROM userGroupCups;
DELETE FROM userGroups;

DELETE FROM matchBets;
DELETE FROM matches;

DELETE FROM cupWinners;
DELETE FROM cupTeams;
DELETE FROM cupTeamBets;
DELETE FROM cups;

DELETE FROM jobLog;
DELETE FROM jobs;

DELETE FROM favorites;
DELETE FROM teams;
DELETE FROM categories;
DELETE FROM sportKinds;
DELETE FROM activities;
DELETE FROM users;

DELETE FROM pointsCalculationStrategies;

ALTER TABLE users AUTO_INCREMENT = 1;
INSERT INTO users(login, password, username) VALUES ('Hakeem', '$2a$10$99kZSOGP68KJzO6Z/2NLruX3H7pVmPoT7r9yeruaWUz42hH/dpwc.', 'Olajuwon');

COMMIT;

