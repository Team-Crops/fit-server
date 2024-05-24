CREATE TABLE IF NOT EXISTS `alarm_type`
(
    `alarm_type_id`     bigint        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`              varchar(255)  NOT NULL,
    `updated_at`        datetime(6)     NOT NULL,
    `created_at`        datetime(6)     NOT NULL,
    `is_deleted`        tinyint(1) NOT NULL DEFAULT false
);


CREATE TABLE IF NOT EXISTS `alarm`
(
    `alarm_id`          bigint        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`           bigint        NOT NULL,
    `alarm_type_id`           bigint        NOT NULL,
    `updated_at`        datetime(6)     NOT NULL,
    `created_at`        datetime(6)     NOT NULL,
    `is_deleted`        tinyint(1) NOT NULL DEFAULT false
);