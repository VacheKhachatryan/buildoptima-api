create table bo_property
(
    ain            varchar(12) primary key,
    municipality   varchar(35) not null,
    locations      json         not null,
    details        jsonb        not null,
    hazards        jsonb        not null,
    zoning_details jsonb        not null,
    version        integer      not null default 0,
    created_at     timestamp    not null,
    updated_at     timestamp
);

create table bo_prop_address
(
    id                      uuid primary key,
    property_ain            varchar(12) not null references bo_property (ain) on delete cascade,
    house_number            varchar(10),
    fraction                varchar(20),
    direction               varchar(50),
    street_name             varchar(50),
    street_suffix           varchar(15),
    street_suffix_direction varchar(15),
    unit                    varchar(15),
    city                    varchar(25),
    state                   varchar(5),
    zip                     varchar(10),
    is_primary              boolean      not null
)

