create table news
(
    id            uuid primary key,
    title         varchar(255) not null,
    summary       varchar(255) not null,
    keywords      varchar(255),
    description   text         not null,
    status        varchar(25)  not null,
    news_category varchar(25)  not null,
    created_by    uuid         not null,
    updated_by    uuid         not null,
    created_at    timestamp    not null,
    updated_at    timestamp
);

create index news_title_index on news (title);
create index news_status_index on news (status);
create index news_category_index on news (news_category);
create index news_created_at_index on news (created_at);
create index news_updated_at_index on news (updated_at);