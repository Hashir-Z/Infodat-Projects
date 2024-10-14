create database LMS;
USE LMS;

CREATE TABLE Users(
  username VARCHAR(400) PRIMARY KEY,
  password VARCHAR(400),
  bisadmin BOOLEAN
);

CREATE TABLE Books (
    title VARCHAR(400),
    author VARCHAR(400),
	isbn VARCHAR(13) primary key,
    publisher VARCHAR(400),
    genre VARCHAR(400),
    publicationdate DATE,
    borrowedby VARCHAR(400),
    FOREIGN KEY (borrowedby) REFERENCES Users(username)
);

drop table books;

insert into users tUsername = "admin" and tPassword = "pass" and 
CREATE USER 'sqluser'@'%' IDENTIFIED 
WITH mysql_native_password BY 'password' ; 
GRANT ALL PRIVILEGES ON *.* TO 'sqluser'@'%' ;
FLUSH PRIVILEGES;
