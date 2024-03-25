create table bo_migration_history
(
    id            uuid primary key,
    file_path     varchar(50) not null,
    processed_at  timestamp    not null,
    failed_at     timestamp,
    failed_reason text
);


create table bo_migration_metadata
(
    id                   uuid primary key,
    migration_history_id uuid         not null,
    ain                  varchar(12) not null,
    addresses            json,
    synced_at            timestamp    not null,
    failed_at            timestamp,
    failed_reason        text
)

