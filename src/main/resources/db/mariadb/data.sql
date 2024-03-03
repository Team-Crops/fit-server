-- Local User Data
insert into user
(user_role, is_open_phone_num, created_at, updated_at, is_deleted)
values ("ADMIN", true, now(), now(), false),
       ("MANAGER", true, now(), now(), false),
       ("MEMBER", true, now(), now(), false),
       ("MEMBER", false, now(), now(), false),
         ("NON_MEMBER", false, now(), now(), false);