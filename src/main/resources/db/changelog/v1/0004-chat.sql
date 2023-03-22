create table chat_rooms
(
    id       bigserial
        primary key,
    admin_id bigint
        constraint fkfomwv3mh229artxlh9qwtltpt
            references users
            on delete set null
);

alter table chat_rooms
    owner to postgres;

create table chat_room_users
(
    chat_room_id bigint not null
        constraint fkmnm9x3nr1fe9ukjgi5ykofvsx
            references chat_rooms
            on delete cascade,
    user_id      bigint not null
        constraint fka4i754uhscevbsye3dmeuma5t
            references users
            on delete cascade,
    primary key (chat_room_id, user_id)
);

alter table chat_room_users
    owner to postgres;

create table messages
(
    id           bigserial
        primary key,
    content      varchar(255),
    chat_room_id bigint
        constraint fk67lyatc9udvn9fgepx08ckmbt
            references chat_rooms
            on delete cascade,
    user_id      bigint
        constraint fkpsmh6clh3csorw43eaodlqvkn
            references users
            on delete set null,
    send_time    timestamp default now()
);

alter table messages
    owner to postgres;

