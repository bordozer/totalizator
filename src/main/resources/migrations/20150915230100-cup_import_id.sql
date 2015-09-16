ALTER TABLE cups ADD cupImportId varchar(100) DEFAULT NULL;

ALTER TABLE categories DROP COLUMN importId;