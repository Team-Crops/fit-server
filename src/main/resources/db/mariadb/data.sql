-- User Data
insert into user
(user_role, is_open_phone_num, created_at, updated_at, is_deleted)
values ('ADMIN', true, now(), now(), false)
;

-- Social User Info Data
insert into social_user_info
(user_id, social_type, social_code)
values (1, 'KAKAO', 'KAKAO_3169170192')
;

