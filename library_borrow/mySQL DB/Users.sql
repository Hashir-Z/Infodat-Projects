USE lms;

add table

SELECT * FROM lms.users where tUsername = "Test";

delete from lms.books where tUsername = "Test";

INSERT INTO Users (tUsername, tPassword, bisAdmin)
VALUES ("admin", "pass", true);