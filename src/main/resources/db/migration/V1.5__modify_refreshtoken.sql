alter table refresh_token drop column id;
alter table refresh_token add constraint refresh_token_pk primary key(refresh_token);