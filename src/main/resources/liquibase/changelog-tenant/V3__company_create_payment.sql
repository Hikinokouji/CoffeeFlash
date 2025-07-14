CREATE TABLE IF NOT EXISTS main.methodPayment(
    id SERIAL primary key,
    method varchar(100) not null unique
);

CREATE TABLE IF NOT EXISTS main.transactionPayment(
    id bigserial primary key,
    methodPayment int not null ,
    created_at timestamp not null ,
    totalPrice numeric(10,2) not null ,

    constraint fk_transaction_method foreign key (methodPayment)
        references main.methodPayment(id)
);

INSERT INTO main.methodPayment(method)
VALUES ('cash'),
       ('creditCart');

CREATE TABLE IF NOT EXISTS main.transactional(
    transaction_id bigint not null ,
    product_id bigint not null ,
    quantity int not null ,
    price_at_purchase numeric(10,2) not null ,

    PRIMARY KEY (transaction_id, product_id),

        constraint fk_transaction_id foreign key (transaction_id)
        references main.transactionPayment(id),
    constraint fk_product foreign key (product_id)
        references main.product(id)
);
