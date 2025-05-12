create table client
(
    id      serial
        primary key,
    name    varchar(100) not null
        constraint client_name_check
            check ((name)::text ~ '^[A-Za-z\s]+$'::text),
    address text         not null,
    email   varchar(255) not null
        constraint client_email_check
            check ((email)::text ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'::text),
    age     integer      not null
        constraint client_age_check
            check ((age >= 0) AND (age <= 120))
);

alter table client
    owner to postgres;

