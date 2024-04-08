-- User Data
insert into user
(user_role, is_open_phone_num, created_at, updated_at, is_deleted)
values ('ADMIN', true, now(), now(), false)
;

insert into user
(user_role, is_open_phone_num, created_at, updated_at, is_deleted)
values ('ADMIN', true, now(), now(), false)
;

insert into user
(user_role, is_open_phone_num, created_at, updated_at, is_deleted)
values ('ADMIN', true, now(), now(), false)
;

-- Social User Info Data
insert into social_user_info
(user_id, social_type, social_code)
values (1, 'KAKAO',  CONCAT('KAKAO_', FLOOR(RAND()*1000000000)))
;

insert into social_user_info
(user_id, social_type, social_code)
values (2, 'GOOGLE',  CONCAT('GOOGLE_', FLOOR(RAND()*1000000000)))
;

insert into social_user_info
(user_id, social_type, social_code)
values (3, 'GOOGLE',  CONCAT('GOOGLE_', FLOOR(RAND()*1000000000)))
;

