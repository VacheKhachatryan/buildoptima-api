CREATE TABLE IF NOT EXISTS refresh_token
(
    id uuid primary key not null ,
    refresh_token varchar(255) not null ,
    user_id UUID not null,
    expires_at timestamp not null
);