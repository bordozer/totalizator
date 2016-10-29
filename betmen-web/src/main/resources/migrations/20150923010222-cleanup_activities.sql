delete from activities;

ALTER TABLE `activities` ADD INDEX `idx_activityEntryId` (`activityEntryId`);