
create table user_to_register
(
    id    bigserial
        primary key,
    email varchar(255),
    name     varchar(255),
    password varchar(255),
    surname  varchar(255),
    username varchar(255),
    code int
);

alter table users
    add column email varchar(255);