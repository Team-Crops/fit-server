CREATE TABLE IF NOT EXISTS `user`
(
    `user_id`           bigint        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_role`         varchar(10)   NOT NULL DEFAULT 'MEMBER',
    `profile_image_url` varchar(2048) NULL,
    `username`          varchar(100) NULL,
    `nickname`          varchar(100)  NULL,
    `phone_number`      varchar(20) NULL,
    `is_open_phone_num` tinyint(1) NOT NULL DEFAULT false,
    `email`             varchar(2048) NULL,
    `updated_at`        datetime(6)     NOT NULL,
    `created_at`        datetime(6)     NOT NULL,
    `is_deleted`        tinyint(1) NOT NULL DEFAULT false
);

CREATE TABLE IF NOT EXISTS `position`
(
    `position_id`  bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `display_name` varchar(20)  NOT NULL,
    `display_name_en` varchar(50) NOT NULL,
    `type`         varchar(20)  NOT NULL,
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
    `is_deleted`   tinyint(1) NOT NULL DEFAULT false,
    CONSTRAINT `uq_region_display_name` UNIQUE (`display_name`)
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
    `position_id`       bigint   NULL,
    `region_id`         bigint   NULL,
    `created_at`        datetime(6) NOT NULL,
    `updated_at`        datetime(6) NOT NULL,
    `is_deleted`        tinyint(1) NOT NULL DEFAULT false,
    CONSTRAINT `fk_user_info_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    CONSTRAINT `fk_user_info_position_id` FOREIGN KEY (`position_id`) REFERENCES `position` (`position_id`),
    CONSTRAINT `fk_user_info_region_id` FOREIGN KEY (`region_id`) REFERENCES `region` (`region_id`)
);

CREATE TABLE IF NOT EXISTS `social_user_info`
(
    `social_user_info_id` bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`             bigint       NOT NULL,
    `social_type`         varchar(20)  NOT NULL,
    `social_code`         varchar(255) NOT NULL,
    `created_at`        datetime(6)     NOT NULL,
    `updated_at`    datetime(6) NOT NULL,
    `is_deleted`    tinyint(1)  NOT NULL DEFAULT false,
    CONSTRAINT `fk_social_user_info_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    INDEX `idx_social_user_info_social_code` (`social_code`)
);

CREATE TABLE IF NOT EXISTS `user_likes`
(
    `user_likes_id`         bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `like_user_id`          bigint      NOT NULL,
    `liked_user_id`         bigint      NOT NULL,
    `created_at`            datetime(6) NOT NULL,
    `updated_at`            datetime(6) NOT NULL,
    `is_deleted`            tinyint(1)  NOT NULL DEFAULT false,
    CONSTRAINT `fk_user_likes_user_id` FOREIGN KEY (`like_user_id`) REFERENCES `user` (`user_id`),
    CONSTRAINT `fk_user_likes_liked_user_id` FOREIGN KEY (`liked_user_id`) REFERENCES `user` (`user_id`),
    INDEX `idx_user_likes_like_user_id_liked_user_id` (`like_user_id`, `liked_user_id`, `is_deleted`),
    INDEX `idx_user_likes_liked_user_id_like_user_id` (`liked_user_id`, `like_user_id`, `is_deleted`)
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
    CONSTRAINT `fk_user_policy_agreement_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
);

CREATE TABLE IF NOT EXISTS `skill`
(
    `skill_id`     bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `display_name` varchar(50) NOT NULL,
    `created_at`   datetime(6)    NOT NULL,
    `updated_at`   datetime(6)    NOT NULL,
    `is_deleted`   tinyint(1) NOT NULL DEFAULT false,
    CONSTRAINT `uq_skill_display_name` UNIQUE (`display_name`)
);

CREATE TABLE IF NOT EXISTS `user_info_skill`
(
    `user_info_skill_id` bigint   NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `skill_id`           bigint   NOT NULL,
    `user_info_id`       bigint   NOT NULL,
    `created_at`         datetime(6) NOT NULL,
    `updated_at`         datetime(6) NOT NULL,
    `is_deleted`         tinyint(1) NOT NULL DEFAULT false,
    CONSTRAINT `fk_user_info_skill_skill_id` FOREIGN KEY (`skill_id`) REFERENCES `skill` (`skill_id`),
    CONSTRAINT `fk_user_info_skill_user_info_id` FOREIGN KEY (`user_info_id`) REFERENCES `user_info` (`user_id`)
);

CREATE TABLE IF NOT EXISTS `skillset`
(
    `skillset_id` bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `position_id` bigint NOT NULL,
    `skill_id`    bigint NOT NULL,
    CONSTRAINT `fk_skillset_position_id` FOREIGN KEY (`position_id`) REFERENCES `position` (`position_id`),
    CONSTRAINT `fk_skillset_skill_id` FOREIGN KEY (`skill_id`) REFERENCES `skill` (`skill_id`)
);

CREATE TABLE IF NOT EXISTS `chat_room`
(
    `chat_room_id`   bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `created_at`     datetime(6)    NOT NULL,
    `updated_at`     datetime(6)    NOT NULL,
    `is_deleted`     tinyint(1) NOT NULL DEFAULT false
);

CREATE TABLE IF NOT EXISTS `message`
(
    `message_id`   bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `chat_room_id` bigint      NOT NULL,
    `user_id`      bigint      NOT NULL,
    `content`      text        NOT NULL,
    `message_type` varchar(20) NOT NULL,
    `created_at`   datetime(6)    NOT NULL,
    `updated_at`   datetime(6)    NOT NULL,
    `is_deleted`   tinyint(1) NOT NULL DEFAULT false,
    CONSTRAINT `fk_message_chat_room_id` FOREIGN KEY (`chat_room_id`) REFERENCES `chat_room` (`chat_room_id`),
    CONSTRAINT `fk_message_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
);

CREATE TABLE IF NOT EXISTS `matching_room`
(
    `matching_room_id`      bigint   NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `chat_room_id` bigint   NOT NULL,
    `host_user_id` bigint   NOT NULL,
    `created_at`   datetime(6) NOT NULL,
    `updated_at`   datetime(6) NOT NULL,
    `is_deleted`   tinyint(1) NOT NULL DEFAULT false,
    `is_completed`   tinyint(1) NOT NULL DEFAULT false,
    `completed_at`   datetime(6) NULL,
    CONSTRAINT `fk_matching_room_chat_room_id` FOREIGN KEY (`chat_room_id`) REFERENCES `chat_room` (`chat_room_id`),
    CONSTRAINT `fk_matching_room_host_user_id` FOREIGN KEY (`host_user_id`) REFERENCES `user` (`user_id`),
    INDEX `idx_matching_room_created_at` (`created_at`)
);

CREATE TABLE IF NOT EXISTS `matching`
(
    `matching_id`     bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`           bigint      NOT NULL,
    `matching_room_id`  bigint      NULL,
    `position_id`       bigint      NOT NULL,
    `created_at`        datetime(6)    NOT NULL,
    `updated_at`        datetime(6)    NOT NULL,
    `is_deleted`   tinyint(1) NOT NULL DEFAULT false,
    `last_batch_at`     datetime(6) NULL,
    `expired_at`        datetime(6) NULL,
    `status`            varchar(20) NULL,
    CONSTRAINT `fk_matching_userid` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    CONSTRAINT `fk_matching_matching_room_id` FOREIGN KEY (`matching_room_id`) REFERENCES `matching_room` (`matching_room_id`),
    CONSTRAINT `fk_matching_position_id` FOREIGN KEY (`position_id`) REFERENCES `position` (`position_id`),
    INDEX `idx_matching_expired_at_desc` (`expired_at` desc)
);

CREATE TABLE IF NOT EXISTS `project`
(
    `project_id`  bigint   NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `chat_room_id` bigint   NOT NULL,
    `created_at`  datetime(6) NOT NULL,
    `updated_at`  datetime(6) NOT NULL,
    `finished_at` datetime(6) NULL,
    `is_deleted`  tinyint(1) NOT NULL DEFAULT false,
    CONSTRAINT `fk_project_chat_room_id` FOREIGN KEY (`chat_room_id`) REFERENCES `chat_room` (`chat_room_id`)
);

CREATE TABLE IF NOT EXISTS `project_member`
(
    `project_member_id` bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `project_id`        bigint NOT NULL,
    `user_id`           bigint NOT NULL,
    `position_id`       bigint NOT NULL,
    `created_at`  datetime(6) NOT NULL,
    `updated_at`  datetime(6) NOT NULL,
    `completed_at` datetime(6) NULL,
    `status`       varchar(20) NOT NULL DEFAULT 'PROJECT_IN_PROGRESS',
    `project_name`  varchar(100) NOT NULL,
    `is_deleted`        tinyint(1) NOT NULL DEFAULT false,
    CONSTRAINT `fk_project_member_project_id` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`),
    CONSTRAINT `fk_project_member_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    CONSTRAINT `fk_project_member_position_id` FOREIGN KEY (`position_id`) REFERENCES `position` (`position_id`)
);

CREATE TABLE IF NOT EXISTS `school`
(
    `school_id`  bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`       varchar(50)  NOT NULL,
    `type`       varchar(100) NOT NULL,
    `created_at` datetime(6)     NOT NULL,
    `updated_at` datetime(6)     NOT NULL,
    `is_deleted` tinyint(1) NOT NULL DEFAULT false,
    CONSTRAINT `uq_school_name_type` UNIQUE (`name`, `type`)
);

CREATE TABLE IF NOT EXISTS `alarm`
(
    `alarm_id`          bigint        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`           bigint        NOT NULL,
    `alarm_case`        varchar(50)    NOT NULL,
    `is_read`          tinyint(1) NOT NULL DEFAULT false,
    `updated_at`        datetime(6)     NOT NULL,
    `created_at`        datetime(6)     NOT NULL,
    `is_deleted`        tinyint(1) NOT NULL DEFAULT false,
    CONSTRAINT `fk_alarm_userid` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
);

CREATE TABLE IF NOT EXISTS `project_report_history`
(
    `project_report_history_id` bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `reporter_user_id`          bigint       NOT NULL,
    `target_user_id`            bigint       NOT NULL,
    `project_id`                bigint       NOT NULL,
    `report_type`               varchar(20)  NOT NULL,
    `description`               varchar(255) NULL,
    `created_at`                datetime(6)     NOT NULL,
    `updated_at`                datetime(6)     NOT NULL,
    `is_deleted`                tinyint(1) NOT NULL DEFAULT false,
    CONSTRAINT `fk_project_report_project_id` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`),
    CONSTRAINT `fk_project_report_reporter_member_id` FOREIGN KEY (`reporter_user_id`) REFERENCES `project_member` (`project_member_id`),
    CONSTRAINT `fk_project_report_reported_member_id` FOREIGN KEY (`target_user_id`) REFERENCES `project_member` (`project_member_id`)
);

CREATE TABLE IF NOT EXISTS `user_block`
(
    `user_block_id` bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`       bigint       NOT NULL,
    `blocked_at`    datetime(6)     NOT NULL,
    `unblocked_at`  datetime(6)     NULL,
    `block_status`  varchar(20)  NOT NULL,
    `created_at`    datetime(6)     NOT NULL,
    `updated_at`    datetime(6)     NOT NULL,
    `is_deleted`    tinyint(1) NOT NULL DEFAULT false,
    CONSTRAINT `fk_user_block_userid` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
);

CREATE TABLE IF NOT EXISTS `user_withdraw`
(
    `user_withdraw_id` bigint    NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`       bigint       NOT NULL,
    `withdraw_reason`           text NULL,
    `is_agree`    tinyint(1)    NOT NULL DEFAULT true,
    `created_at`                datetime(6)     NOT NULL,
    `updated_at`                datetime(6)     NOT NULL,
    `is_deleted`                tinyint(1) NOT NULL DEFAULT false,
    CONSTRAINT `fk_user_withdraw_userid` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
);


CREATE TABLE IF NOT EXISTS `chat_room_user`
(
    `chat_room_user_id` bigint   NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `chat_room_id`      bigint   NOT NULL,
    `user_id`           bigint   NOT NULL,
    `last_checked_message_id`    bigint     NULL,
    `created_at`        datetime NOT NULL,
    `updated_at`        datetime NOT NULL,
    `is_deleted`        tinyint(1) NOT NULL DEFAULT false,
    CONSTRAINT `fk_chat_room_user_chat_room_id` FOREIGN KEY (`chat_room_id`) REFERENCES `chat_room` (`chat_room_id`),
    CONSTRAINT `fk_chat_room_user_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    CONSTRAINT `fk_chat_room_user_last_checked_message_id` FOREIGN KEY (`last_checked_message_id`) REFERENCES `message` (`message_id`)
);