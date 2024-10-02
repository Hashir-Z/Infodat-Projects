create database LMS;
USE LMS;

CREATE TABLE Users(
  tUsername VARCHAR(400) PRIMARY KEY,
  tPassword VARCHAR(400),
  bisAdmin BOOLEAN
);

CREATE TABLE Books (
    Title VARCHAR(400),
    Author VARCHAR(400),
    ISBN VARCHAR(13),
    Publisher VARCHAR(400),
    Genre VARCHAR(400),
    PublicationDate DATE,
    BorrowedBy VARCHAR(400),
    FOREIGN KEY (BorrowedBy) REFERENCES Users(tUsername)
);
  
CREATE USER 'sqluser'@'%' IDENTIFIED 
WITH mysql_native_password BY 'password' ; 
GRANT ALL PRIVILEGES ON *.* TO 'sqluser'@'%' ;
FLUSH PRIVILEGES;
