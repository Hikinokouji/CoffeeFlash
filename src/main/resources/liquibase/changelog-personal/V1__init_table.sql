create schema if not exists personal;

create table if not exists personal.users
(
    id bigserial primary key,
    phoneNumber varchar(255) not null unique,
    password varchar(255) not null
);

create table if not exists personal.companies
(
    company_uuid varchar(255) primary key not null unique,
    createDate timestamp
);

create table if not exists personal.user_company
(
    user_id bigint not null,
    company_uuid varchar(255) not null,

    constraint pk_user_company primary key (user_id, company_uuid),

    constraint fk_user foreign key (user_id) references personal.users(id) on delete cascade,
    constraint fk_company foreign key (company_uuid) references personal.companies(company_uuid) on delete cascade
)