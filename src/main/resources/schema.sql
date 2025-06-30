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

-- create table if not exists user_company
-- (
--     user_id bigint not null,
--     company_id bigint not null,
--     primary key (user_id, company_id),
--     constraint fk_user_company_user foreign key (user_id) references users (id) on delete cascade on update no action,
--     constraint fk_user_company_company foreign key (company_id) references company (id) on delete cascade on update no action
-- );