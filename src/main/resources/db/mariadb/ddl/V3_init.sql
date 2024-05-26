CREATE TABLE IF NOT EXISTS `alarm`
(
    `alarm_id`          bigint        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`           bigint        NOT NULL,
    `alarm_case`        varchar(50)    NOT NULL,
    `is_read`          tinyint(1) NOT NULL DEFAULT false,
    `updated_at`        datetime(6)     NOT NULL,
    `created_at`        datetime(6)     NOT NULL,
    `is_deleted`        tinyint(1) NOT NULL DEFAULT false
);