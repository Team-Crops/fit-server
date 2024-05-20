CREATE TABLE IF NOT EXISTS `chat_room_user`
(
    `chat_room_user_id` bigint   NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `chat_room_id`      bigint   NOT NULL,
    `user_id`           bigint   NOT NULL,
    `last_checked_message_id`    bigint   NOT NULL,
    `created_at`        datetime NOT NULL,
    `updated_at`        datetime NOT NULL,
    `is_deleted`        tinyint(1) NOT NULL DEFAULT false,
    CONSTRAINT `fk_chatroomuser_chatroomid` FOREIGN KEY (`chat_room_id`) REFERENCES `chat_room` (`chat_room_id`),
    CONSTRAINT `fk_chatroomuser_userid` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    CONSTRAINT `fk_chatroomuser_lastcheckedmessageid` FOREIGN KEY (`last_checked_message_id`) REFERENCES `message` (`message_id`)
);

alter table `social_user_info`
    add `created_at`    datetime(6) NOT NULL;
alter table `social_user_info`
    add `updated_at`    datetime(6) NOT NULL;
alter table `social_user_info`
    add `is_deleted`    tinyint(1)  NOT NULL DEFAULT false;



