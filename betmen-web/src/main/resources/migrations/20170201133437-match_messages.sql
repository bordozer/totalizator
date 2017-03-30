CREATE TABLE MATCH_MESSAGE
(
  ID       INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  MATCH_ID INT(11)             NOT NULL,
  USER_ID  INT(11)             NOT NULL,
  MESSAGE_TIME DATETIME NOT NULL,
  MESSAGE  TINYTEXT,
  CONSTRAINT FK_USER_ID FOREIGN KEY (USER_ID) REFERENCES users (id) ON DELETE CASCADE,
  CONSTRAINT FK_MATCH_ID FOREIGN KEY (MATCH_ID) REFERENCES matches (id) ON DELETE CASCADE
);
CREATE INDEX IDX_MESSAGE_MATCH_ID ON MATCH_MESSAGE (MATCH_ID);
CREATE INDEX IDX_MESSAGE_USER_ID ON MATCH_MESSAGE (USER_ID);

