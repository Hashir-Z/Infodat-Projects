USE lms;

add table

SELECT * FROM lms.users where tUsername = "Test";

delete from lms.books where tUsername = "Test";

INSERT INTO Users (tUsername, tPassword, bisAdmin)
VALUES ("admin", "pass", true);

ALTER TABLE Users RENAME COLUMN tUsername TO username;
ALTER TABLE Users RENAME COLUMN tPassword TO password;
ALTER TABLE Users RENAME COLUMN bisAdmin TO isadmin;
