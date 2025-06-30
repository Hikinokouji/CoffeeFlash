create table if not exists maininfo.users
(
    id bigserial primary key,
    phoneNumber varchar(255) not null unique,
    password varchar(255) not null
);

create table if not exists maininfo.company
(
    id bigserial primary key,
    uuid varchar(255) not null unique
);