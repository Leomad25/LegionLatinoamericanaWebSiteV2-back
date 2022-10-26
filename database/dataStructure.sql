--
-- basic configuration
--

DROP DATABASE IF EXISTS `legion_latinoamericana_db`;
CREATE DATABASE `legion_latinoamericana_db`;

DROP USER IF EXISTS 'legionlatinoamericana'@'localhost';
CREATE USER 'legionlatinoamericana'@'localhost' IDENTIFIED BY 'legionlatinoamericana';
GRANT EXECUTE ON `legion_latinoamericana_db`.* TO 'legionlatinoamericana'@'localhost';

USE `legion_latinoamericana_db`;

--
-- creation of tables
--

CREATE TABLE `crl_params` (
  `key` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8_bin NOT NULL,
  `value` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`key`),
  UNIQUE KEY `key_UNIQUE` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

CREATE TABLE `grl_permissions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `label` VARCHAR(45) NOT NULL,
  `weight` INT(3) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `grl_permissions_weight_UNIQUE` (`weight` ASC) VISIBLE,
  UNIQUE INDEX `grl_permissions_label_UNIQUE` (`label` ASC) VISIBLE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

CREATE TABLE `grl_status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` NVARCHAR(5) NOT NULL,
  `description` TEXT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `grl_status_code_UNIQUE` (`code` ASC) VISIBLE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

CREATE TABLE `grl_users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `permission` INT DEFAULT 1,
  `status` INT DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `grl_users_username_UNIQUE` (`username` ASC) VISIBLE,
  CONSTRAINT `fk/grl_users/grl_permissions`
	FOREIGN KEY (`permission` ASC)
	REFERENCES `legion_latinoamericana_db`.`grl_permissions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk/grl_users/grl_status`
	FOREIGN KEY (`status` ASC)
    REFERENCES `legion_latinoamericana_db`.`grl_status` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

--
-- insert initial data
--

INSERT INTO `legion_latinoamericana_db`.`grl_permissions` (`id`, `label`, `weight`) VALUES ('1', 'NONE', '0');
INSERT INTO `legion_latinoamericana_db`.`grl_permissions` (`id`, `label`, `weight`) VALUES ('2', 'SOLDIER', '1');
INSERT INTO `legion_latinoamericana_db`.`grl_permissions` (`id`, `label`, `weight`) VALUES ('3', 'SQUAD LEADER', '10');
INSERT INTO `legion_latinoamericana_db`.`grl_permissions` (`id`, `label`, `weight`) VALUES ('4', 'PLATOON LEADER', '90');
INSERT INTO `legion_latinoamericana_db`.`grl_permissions` (`id`, `label`, `weight`) VALUES ('5', 'HELPER', '100');
INSERT INTO `legion_latinoamericana_db`.`grl_permissions` (`id`, `label`, `weight`) VALUES ('6', 'MODERATOR', '200');
INSERT INTO `legion_latinoamericana_db`.`grl_permissions` (`id`, `label`, `weight`) VALUES ('7', 'ADMINISTRATOR', '900');
INSERT INTO `legion_latinoamericana_db`.`grl_permissions` (`id`, `label`, `weight`) VALUES ('8', 'DEVELOPER', '999');

INSERT INTO `legion_latinoamericana_db`.`grl_status` (`id`, `code`, `description`) VALUES ('1', 'ICONF', 'INITIAL CONFIGURATION');
INSERT INTO `legion_latinoamericana_db`.`grl_status` (`id`, `code`, `description`) VALUES ('2', 'INCOM', 'USER INCOMPLETE');
INSERT INTO `legion_latinoamericana_db`.`grl_status` (`id`, `code`, `description`) VALUES ('3', 'NOACT', 'USER NOT ACTIVATED');
INSERT INTO `legion_latinoamericana_db`.`grl_status` (`id`, `code`, `description`) VALUES ('4', 'APPRO', 'USER OK');
INSERT INTO `legion_latinoamericana_db`.`grl_status` (`id`, `code`, `description`) VALUES ('5', 'PAUSE', 'USER IN PROCESS');
INSERT INTO `legion_latinoamericana_db`.`grl_status` (`id`, `code`, `description`) VALUES ('6', 'UBANN', 'USER BLOQUED');

--
-- creation of procedure
--

DELIMITER $$

--
-- params
--
CREATE PROCEDURE `CREATE_PARAM` (IN _key VARCHAR(100), IN _value VARCHAR(200))
BEGIN
	INSERT INTO `legion_latinoamericana_db`.`crl_params` (`key`, `value`) VALUES (UPPER(_key), UPPER(_value));
END$$

CREATE PROCEDURE `GET_PARAM` (IN _key VARCHAR(100))
BEGIN
	SELECT
		`value` AS 'PARAM_VALUE'
    FROM `legion_latinoamericana_db`.`crl_params`
    WHERE (`key` = UPPER(_key));
END$$

--
-- permissions
--
CREATE PROCEDURE `CREATE_PERMISSION` (IN _label VARCHAR(45), IN _weight INT(3))
BEGIN
	INSERT INTO `legion_latinoamericana_db`.`grl_permissions` (`label`, `weight`) VALUES (UPPER(_label), _weight);
END$$

CREATE PROCEDURE `GET_PERMISSIONS` ()
BEGIN
	SELECT
		`id` AS 'PERMIT_ID',
        `label` AS 'PERMIT_LABEL',
        `weight` AS 'PERMIT_WEIGHT'
    FROM `legion_latinoamericana_db`.`grl_permissions`;
END$$

CREATE PROCEDURE `GET_PERMISSION` (IN _id INT)
BEGIN
	SELECT
		`id` AS 'PERMIT_ID',
        `label` AS 'PERMIT_LABEL',
        `weight` AS 'PERMIT_WEIGHT'
    FROM `legion_latinoamericana_db`.`grl_permissions`
    WHERE (`id` = _id);
END$$

--
-- status
--
CREATE PROCEDURE `CREATE_STATUS` (IN _code NVARCHAR(5), IN _description TEXT)
BEGIN
	INSERT INTO `legion_latinoamericana_db`.`grl_status` (`code`, `description`) VALUES (UPPER(_code), UPPER(_description));
END$$

CREATE PROCEDURE `GET_STATUS` ()
BEGIN
	SELECT
		`id` AS 'STATE_ID',
        `code` AS 'STATE_CODE',
        `description` AS 'STATE_DESC'
    FROM `legion_latinoamericana_db`.`grl_status`;
END$$

CREATE PROCEDURE `GET_STATE` (IN _id INT)
BEGIN
	SELECT
		`id` AS 'STATE_ID',
        `code` AS 'STATE_CODE',
        `description` AS 'STATE_DESC'
    FROM `legion_latinoamericana_db`.`grl_status`
    WHERE (`id` = _id);
END$$

--
-- users
--
CREATE PROCEDURE `CREATE_USER` (IN _username VARCHAR(45), IN _password VARCHAR(255), IN _email VARCHAR(255))
BEGIN
	INSERT INTO `legion_latinoamericana_db`.`grl_users` (`username`, `password`, `email`) VALUES (UPPER(_username), _password, UPPER(_email));
    SELECT
		`grl_users`.`id` AS "USER_ID"
	FROM `legion_latinoamericana_db`.`grl_users`
    WHERE `grl_users`.`username` = UPPER(_username);
END$$

CREATE PROCEDURE `GET_USER` (IN _id BIGINT)
BEGIN
	SELECT
		`grl_users`.`id` AS 'USER_ID',
		`grl_users`.`username` AS 'USER_NAME',
        `grl_users`.`email` AS 'USER_EMAIL',
		`grl_permissions`.`label` AS 'PERM_LABEL',
		`grl_permissions`.`weight` AS 'PERM_WEIGHT',
		`grl_status`.`code` AS 'STAT_CODE',
		`grl_status`.`description` AS 'STAT_DESC'
	FROM `legion_latinoamericana_db`.`grl_users`
		INNER JOIN `grl_permissions` ON `grl_users`.`permission`=`grl_permissions`.`id`
		INNER JOIN `grl_status` ON `grl_users`.`status`=`grl_status`.`id`
	WHERE `grl_users`.`id`=_id;
END$$

CREATE PROCEDURE `GET_USER_PASS` (IN _username VARCHAR(45))
BEGIN
	SELECT
		`grl_users`.`id` AS 'USER_ID',
		`grl_users`.`password` AS 'USER_PASSWORD'
	FROM `legion_latinoamericana_db`.`grl_users`
	WHERE `grl_users`.`username`=UPPER(_username);
END$$

CREATE PROCEDURE `SET_USER_PERMISSION` (IN _id BIGINT, IN _permission INT)
BEGIN
	UPDATE `legion_latinoamericana_db`.`grl_users` SET `permission` = _permission WHERE (`id` = _id);
    CALL `GET_USER`(_id);
END$$

CREATE PROCEDURE `SET_USER_STATUS` (IN _id BIGINT, IN _status INT)
BEGIN
	UPDATE `legion_latinoamericana_db`.`grl_users` SET `status` = _status WHERE (`id` = _id);
    CALL `GET_USER`(_id);
END$$

CREATE PROCEDURE `IS_USER_EXISTS` (IN _username VARCHAR(45))
BEGIN
	SELECT
		COUNT(`grl_users`.`id`) AS 'RESULT'
	FROM `legion_latinoamericana_db`.`grl_users`
	WHERE `grl_users`.`username`=UPPER(_username);
END$$

DELIMITER ;
