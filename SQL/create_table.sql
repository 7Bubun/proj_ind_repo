USE TERMINARZ;

CREATE TABLE EVENTS_TBL(
ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
DEADLINE DATETIME NOT NULL,
NAME_OF_EVENT VARCHAR(20) CHARACTER SET UTF8MB4	NOT NULL
);
