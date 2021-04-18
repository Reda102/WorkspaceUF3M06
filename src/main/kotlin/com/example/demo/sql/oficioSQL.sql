DROP DATABASE IF EXISTS oficio;
CREATE DATABASE oficio;
DROP USER IF EXISTS root;
CREATE USER root IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON oficio.* TO root WITH GRANT OPTION;
USE oficio;

CREATE TABLE person
	(
    id		INT AUTO_INCREMENT,
    name VARCHAR(32) not null,
	title VARCHAR(64) NOT NULL,
    date_deleted    LONG,
    PRIMARY KEY(id)
	)ENGINE=InnoDB
	;

INSERT INTO person VALUE (1,'Axel','Diseñador web', 0);
INSERT INTO person VALUE (2,'Pere','Programador BACK-END', 0);
INSERT INTO person VALUE (3,'Fernando','Director del proyecto', 0);


SELECT * FROM person;