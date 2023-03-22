create table posts
(
    id         bigserial
        primary key,
    created_at timestamp,
    text       varchar(255),
    author_id  bigint
        constraint fk6xvn0811tkyo3nfjk2xvqx6ns
            references users
            on delete cascade
);

alter table posts
    owner to postgres;

create table pictures
(
    id      bigserial
        primary key,
    path    varchar(255),
    post_id bigint
        constraint fkkrfm1myqaikatwd5kpw7rj0ip
            references posts
            on delete cascade
);

alter table pictures
    owner to postgres;

create table likes
(
    id           bigserial
        primary key,
    post_id      bigint
        constraint fkry8tnr4x2vwemv2bb0h5hyl0x
            references posts
            on delete cascade,
    who_likes_id bigint
        constraint fk22e0uapy9me33p2cuh0uns91b
            references users
            on delete cascade
);

alter table likes
    owner to postgres;

create table comments
(
    id         bigserial
        primary key,
    created_at timestamp,
    text       varchar(255),
    author_id  bigint
        constraint fkn2na60ukhs76ibtpt9burkm27
            references users
            on delete cascade,
    post_id    bigint
        constraint fkh4c7lvsc298whoyd4w9ta25cr
            references posts
            on delete cascade
);

alter table comments
    owner to postgres;

create table comment_likes
(
    id         bigserial
        primary key,
    author_id  bigint
        constraint fklvj59m0dqx03dqly3kh3tjecf
            references users
            on delete cascade,
    comment_id bigint
        constraint fk3wa5u7bs1p1o9hmavtgdgk1go
            references comments
            on delete cascade
);

alter table comment_likes
    owner to postgres;

create table hashtag
(
    id      bigserial
        primary key,
    content varchar(255),
    post_id bigint
        constraint fk9g0e08aher34n7buwmtv4y15
            references posts
            on delete cascade
);

alter table hashtag
    owner to postgres;

create table marks
(
    id         bigserial
        primary key,
    height     integer,
    username   varchar(255),
    width      integer,
    picture_id bigint
        constraint marks_pictures_id_fk
            references pictures
            on delete cascade
);

alter table marks
    owner to postgres;

create table stories
(
    id          bigserial
        primary key,
    created_at  timestamp,
    is_expired  boolean,
    picture_url varchar(255),
    author_id   bigint
        constraint fka4ak6n0w5tjjxht2tky5iovmc
            references users
            on delete cascade
);

alter table stories
    owner to postgres;

create table sponsored_posts
(
    id         bigserial
        primary key,
    post_id    bigint
        constraint fk4j340m6hnk3mfrik2teqi40rq
            references posts
            on delete cascade,
    sponsor_id bigint
        constraint fkao7vnkdnmbvdc010fxq60hn27
            references users
            on delete set null
);

alter table sponsored_posts
    owner to postgres;

