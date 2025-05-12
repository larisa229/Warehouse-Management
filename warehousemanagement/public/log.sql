create table log
(
    id           serial
        primary key,
    order_id     integer        not null,
    client_name  varchar(100)   not null,
    product_name varchar(100)   not null,
    quantity     integer        not null,
    total_price  numeric(10, 2) not null,
    order_date   timestamp default CURRENT_TIMESTAMP
);

alter table log
    owner to postgres;

