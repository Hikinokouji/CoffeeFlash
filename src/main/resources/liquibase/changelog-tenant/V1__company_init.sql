create schema if not exists main;

create table if not exists main.product
(
    id bigserial primary key,
    productName varchar(255) not null unique,
    price int
)