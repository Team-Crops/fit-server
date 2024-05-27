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

CREATE TABLE IF NOT EXISTS `user_withdraw`
(
    `user_withdraw_id`  bigint          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`           bigint          NOT NULL,
    `is_agree`          tinyint(1)      NOT NULL DEFAULT true,
    `withdraw_reason`   varchar(255)    NULL,
    `updated_at`        datetime(6)     NOT NULL,
    `created_at`        datetime(6)     NOT NULL,
    `is_deleted`        tinyint(1) NOT NULL DEFAULT false
);

CREATE TABLE IF NOT EXISTS `report`
(
    `report_id`             bigint          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `project_id`            bigint               NOT NULL,
    `reporter_member_id`    bigint               NOT NULL,
    `reported_member_id`    bigint               NOT NULL,
    `type`                  varchar(30)          NOT NULL,
    `content`               text                 NULL,
    `created_at`            datetime(6)          NOT NULL,
    `updated_at`            datetime(6)          NOT NULL,
    `is_deleted`            tinyint(1)           NOT NULL DEFAULT false
);

