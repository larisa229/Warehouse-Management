create table "order"
(
    id         integer   default nextval('orders_id_seq'::regclass) not null
        constraint orders_pkey
            primary key,
    client_id  integer                                              not null
        constraint orders_client_id_fkey
            references client
            on delete cascade,
    product_id integer                                              not null
        constraint orders_product_id_fkey
            references product
            on delete cascade,
    quantity   integer                                              not null
        constraint orders_quantity_check
            check (quantity > 0),
    order_date timestamp default CURRENT_TIMESTAMP                  not null
);

alter table "order"
    owner to postgres;

