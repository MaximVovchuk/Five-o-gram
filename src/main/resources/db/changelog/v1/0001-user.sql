create table users
(
    id       bigserial
        primary key,
    name     varchar(255),
    password varchar(255),
    role     bigint,
    surname  varchar(255),
    username varchar(255)
);

alter table users
    owner to postgres;

create table avatars
(
    id      bigserial
        primary key,
    path    varchar(255),
    user_id bigint
        constraint fkdh0goytewcg1geffkf1clp4kh
            references users
            on delete cascade
);

alter table avatars
    owner to postgres;

create table subscriptions
(
    id        bigserial
        primary key,
    friend_id bigint
        constraint fkkx5e3k252c7ectoxf2iqu9x2d
            references users
            on delete cascade,
    owner_id  bigint
        constraint subscriptions_users_id_fk
            references users
            on delete cascade
);

alter table subscriptions
    owner to postgres;

create table notifications
(
    id         bigserial
        primary key,
    created_at timestamp,
    entity_id  bigint,
    type       bigint,
    user_id    bigint
        constraint fk9y21adhxn0ayjhfocscqox7bh
            references users
            on delete cascade
);

alter table notifications
    owner to postgres;

