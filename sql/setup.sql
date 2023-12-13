CREATE DATABASE IF NOT EXISTS `movie_rental`;

USE `movie_rental`;

CREATE TABLE IF NOT EXISTS `user` (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(128) NOT NULL UNIQUE,
    password VARCHAR(40) NOT NULL,
    first_name VARCHAR(128) NOT NULL,
    last_name VARCHAR(128) NOT NULL,
    created_on DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS `movie` (
   id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   title VARCHAR(256) NOT NULL,
   price FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS `rental` (
   id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   user_id INT NOT NULL,
   movie_id INT NOT NULL,
   started_on DATETIME NOT NULL,
   ended_on DATETIME,
   total_price FLOAT,
   FOREIGN KEY(user_id) REFERENCES user(id),
   FOREIGN KEY(movie_id) REFERENCES movie(id)
);


INSERT INTO `user`(id, username, password, first_name, last_name, created_on)
VALUES
(
    1,
    "SamWeiss123",
    "C4A265CA6DC31ED4A47A6047E92C5E3790135AF9",
    "Sam",
    "Weiss",
    CURRENT_TIME
),
(
    2,
    "John101",
    "C4A265CA6DC31ED4A47A6047E92C5E3790135AF9",
    "John",
    "Doe",
    CURRENT_TIME
),
(
    3,
    "Jane102",
    "C4A265CA6DC31ED4A47A6047E92C5E3790135AF9",
    "Jane",
    "Doe",
    CURRENT_TIME
);
