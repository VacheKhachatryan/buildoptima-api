CREATE TABLE IF NOT EXISTS users
(
    id UUID primary key not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    phone varchar(255) unique,
    email varchar(255) unique not null,
    password varchar(255) not null,
    role varchar(255) not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    enabled boolean not null
);