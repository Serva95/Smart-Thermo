CREATE DATABASE IF NOT EXISTS thermo;

USE thermo;

CREATE TABLE IF NOT EXISTS temps ( 
	measureId INT NOT NULL AUTO_INCREMENT ,
	temp DOUBLE NOT NULL ,
	hum DOUBLE NOT NULL ,
	date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,
	thermoId INT(10) NULL DEFAULT NULL ,
	PRIMARY KEY (measureId)
	) ENGINE = InnoDB DEFAULT CHARSET = utf8;
	
CREATE TABLE IF NOT EXISTS mediums (
	mediumDay DATE NOT NULL ,
	mediumtemp DOUBLE NOT NULL,
	mediumhum DOUBLE NOT NULL,
	measurenumber SMALLINT(3) NOT NULL,
	thermoId INT(10) NULL DEFAULT NULL ,
	PRIMARY KEY (mediumDay)
	) ENGINE = InnoDB DEFAULT CHARSET = utf8;