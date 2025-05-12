create table product
(
    id            serial
        primary key,
    product_name  varchar(100)   not null
        unique,
    price         numeric(10, 2) not null
        constraint product_price_check
            check (price > (0)::numeric),
    current_stock integer        not null
        constraint product_current_stock_check
            check (current_stock >= 0)
);

alter table product
    owner to postgres;

