create table post_reports
(
    id          bigserial
        primary key,
    text        varchar(255),
    post_id     bigint
        constraint fk7ccpkj5jys037f9pq98l31ya2
            references posts,
    report_date timestamp default now()
);

alter table post_reports
    owner to postgres;

create table posts_to_ban
(
    id             integer generated always as identity,
    post_report_id integer
        references post_reports
);

alter table posts_to_ban
    owner to postgres;

create table story_reports
(
    id          bigserial
        primary key,
    text        varchar(255),
    story_id    bigint
        constraint fknrojuc7tt2mlnwc9bkkdy7kcp
            references stories,
    report_date timestamp default now()
);

alter table story_reports
    owner to postgres;


create table stories_to_ban
(
    id              integer generated always as identity,
    story_report_id integer
        references story_reports
);

alter table stories_to_ban
    owner to postgres;

