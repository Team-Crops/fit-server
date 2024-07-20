-- Alarm Data

insert into `alarm_type`
(name, description, created_at, updated_at, is_deleted)
values ('ALARM_TYPE_1', 'ALARM_TYPE_1', now(), now(), false)

insert into `alaram`
(user_id, alarm_type_id, is_read, created_at, updated_at, is_deleted)
values (1, 1, false, now(), now(), false)
;