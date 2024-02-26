CREATE TABLE IF NOT EXISTS `user`
(
    `user_id`           bigint        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_role`         varchar(10)   NOT NULL DEFAULT 'MEMBER',
    `profile_image_url` varchar(2048) NULL,
    `username`          varchar(100) NULL,
    `nickname`          varchar(100)  NOT NULL,
    `phone_number`      varchar(20) NULL,
    `is_open_phone_num` tinyint(1) NOT NULL DEFAULT false,
    `email`             varchar(2048) NOT NULL,
    `updated_at`        datetime(6)     NOT NULL,
    `created_at`        datetime(6)     NOT NULL,
    `is_deleted`        tinyint(1) NOT NULL DEFAULT false
);

CREATE TABLE IF NOT EXISTS `position`
(
    `position_id`  bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `display_name` varchar(20)  NOT NULL,
    `image_url`   varchar(2048)     NOT NULL,
    `created_at`   datetime(6)      NOT NULL,
    `updated_at`   datetime(6)      NOT NULL,
    `is_deleted`   tinyint(1)   NOT NULL DEFAULT false
);

CREATE TABLE IF NOT EXISTS `region`
(
    `region_id`    bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `display_name` varchar(100) NOT NULL,
    `created_at`   datetime(6)     NOT NULL,
    `updated_at`   datetime(6)     NOT NULL,
    `is_deleted`   tinyint(1) NOT NULL DEFAULT false
);

CREATE TABLE IF NOT EXISTS `user_info`
(
    `user_id`           bigint   NOT NULL PRIMARY KEY,
    `portfolio_url`     varchar(2048) NULL,
    `project_count`     integer NULL,
    `activity_hour`     smallint NULL,
    `introduce`         varchar(255) NULL,
    `link_json`         text NULL,
    `background_status` varchar(30) NULL,
    `career`            varchar(255) NULL,
    `education`         varchar(255) NULL,
    `is_open_profile`   tinyint(1) NOT NULL DEFAULT false,
    `status`            varchar(20) NULL DEFAULT 'complete',
    `position_id`       bigint   NOT NULL,
    `region_id`         bigint   NOT NULL,
    `created_at`        datetime(6) NOT NULL,
    `updated_at`        datetime(6) NOT NULL,
    `is_deleted`        tinyint(1) NOT NULL DEFAULT false,
    CONSTRAINT `fk_userinfo_userid` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    CONSTRAINT `fk_userinfo_positionid` FOREIGN KEY (`position_id`) REFERENCES `position` (`position_id`),
    CONSTRAINT `fk_userinfo_regionid` FOREIGN KEY (`region_id`) REFERENCES `region` (`region_id`)
);

CREATE TABLE IF NOT EXISTS `social_user_info`
(
    `social_user_info_id` bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`             bigint       NOT NULL,
    `social_type`         varchar(20)  NOT NULL,
    `social_code`         varchar(255) NOT NULL,
    CONSTRAINT `fk_socialuserinfo_userid` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    CONSTRAINT `uq_socialuserinfo_social_code` UNIQUE (`social_code`)
);

CREATE TABLE IF NOT EXISTS `user_policy_agreement`
(
    `user_policy_agreement_id` bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`                  bigint       NOT NULL,
    `policy_type`              varchar(100) NOT NULL,
    `version`                  varchar(100) NOT NULL,
    `is_agree`                 tinyint(1) NOT NULL DEFAULT false,
    `updated_at`               datetime(6)     NOT NULL,
    `created_at`               datetime(6)     NOT NULL,
    `is_deleted`               tinyint(1) NOT NULL DEFAULT false,
    CONSTRAINT `fk_userpolicyagreement_userid` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
);

CREATE TABLE IF NOT EXISTS `skill`
(
    `skill_id`     bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `display_name` varchar(50) NOT NULL,
    `created_at`   datetime(6)    NOT NULL,
    `updated_at`   datetime(6)    NOT NULL,
    `is_deleted`   tinyint(1) NOT NULL DEFAULT false,
    CONSTRAINT `skill_uq_display_name` UNIQUE (`display_name`)
);

CREATE TABLE IF NOT EXISTS `user_info_skill`
(
    `user_info_skill_id` bigint   NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `skill_id`           bigint   NOT NULL,
    `user_info_id`       bigint   NOT NULL,
    `created_at`         datetime(6) NOT NULL,
    `updated_at`         datetime(6) NOT NULL,
    `is_deleted`         tinyint(1) NOT NULL DEFAULT false
);

CREATE TABLE IF NOT EXISTS `skillset`
(
    `skillset_id` bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `position_id` bigint NOT NULL,
    `skill_id`    bigint NOT NULL
);

CREATE TABLE IF NOT EXISTS `chat_room`
(
    `chat_room_id`   bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `created_at`     datetime(6)    NOT NULL,
    `updated_at`     datetime(6)    NOT NULL,
    `chat_room_type` varchar(20) NOT NULL,
    `is_deleted`     tinyint(1) NOT NULL DEFAULT false
);

CREATE TABLE IF NOT EXISTS `message`
(
    `message_id`   bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `chat_room_id` bigint      NOT NULL,
    `user_id`      bigint      NOT NULL,
    `content`      text        NOT NULL,
    `message_type` varchar(20) NOT NULL DEFAULT 'text',
    `created_at`   datetime(6)    NOT NULL,
    `updated_at`   datetime(6)    NOT NULL,
    `is_deleted`   tinyint(1) NOT NULL DEFAULT false
);

CREATE TABLE IF NOT EXISTS `user_match`
(
    `user_match_id`     bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`           bigint      NOT NULL,
    `room_id`           bigint NULL,
    `position_id`       bigint      NOT NULL,
    `position_skill_id` bigint      NOT NULL,
    `is_host`           tinyint(1) NOT NULL DEFAULT false,
    `created_at`        datetime(6)    NOT NULL,
    `updated_at`        datetime(6)    NOT NULL,
    `position`          varchar(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS `room`
(
    `room_id`      bigint   NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `chat_room_id` bigint   NOT NULL,
    `created_at`   datetime(6) NOT NULL,
    `updated_at`   datetime(6) NOT NULL,
    `is_deleted`   tinyint(1) NOT NULL DEFAULT false,
    `is_matched`   tinyint(1) NOT NULL DEFAULT false,
    `matched_at`   datetime(6) NULL
);

CREATE TABLE IF NOT EXISTS `project`
(
    `project_id`  bigint   NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `created_at`  datetime(6) NOT NULL,
    `updated_at`  datetime(6) NOT NULL,
    `finished_at` datetime(6) NULL,
    `is_deleted`  tinyint(1) NOT NULL DEFAULT false
);

CREATE TABLE IF NOT EXISTS `project_member`
(
    `project_member_id` bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `project_id`        bigint NOT NULL,
    `user_id`           bigint NOT NULL,
    `position_id`       bigint NOT NULL,
    `position_skill_id` bigint NOT NULL,
    `is_deleted`        tinyint(1) NOT NULL DEFAULT false,
    `is_ proceed`       tinyint(1) NOT NULL DEFAULT true
);

CREATE TABLE IF NOT EXISTS `school`
(
    `school_id`  bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`       varchar(50)  NOT NULL,
    `type`       varchar(100) NOT NULL,
    `created_at` datetime(6)     NOT NULL,
    `updated_at` datetime(6)     NOT NULL,
    `is_deleted` tinyint(1) NOT NULL DEFAULT false
);

CREATE TABLE IF NOT EXISTS `alarm`
(
    `alarm_id`      bigint   NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`       bigint   NOT NULL,
    `alarm_type_id` bigint   NOT NULL,
    `created_at`    datetime(6) NOT NULL,
    `updated_at`    datetime(6) NOT NULL,
    `is_deleted`    tinyint(1) NOT NULL DEFAULT false,
    `is_readed`     tinyint(1) NOT NULL DEFAULT false
);

CREATE TABLE IF NOT EXISTS `alarm_type`
(
    `alarm_type_id` bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `content`       varchar(255) NOT NULL,
    `created_at`    datetime(6)     NOT NULL,
    `updated_at`    datetime(6)     NOT NULL,
    `is_deleted`    tinyint(1) NOT NULL DEFAULT false
);

CREATE TABLE IF NOT EXISTS `report`
(
    `report_id`          bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `project_id`         bigint      NOT NULL,
    `reporter_member_id` bigint      NOT NULL,
    `reported_member_id` bigint      NOT NULL,
    `type`               varchar(30) NOT NULL,
    `content`            text NULL,
    `created_at`         datetime(6)    NOT NULL,
    `updated_at`         datetime(6)    NOT NULL,
    `is_deleted`         tinyint(1) NOT NULL DEFAULT false
);
