drop table if exists user;
drop table if exists article;
drop table if exists reply;

create table user
(
    id       varchar(100) primary key,
    password varchar(100) not null,
    name     varchar(100) not null,
    email    varchar(100) not null
);

create table article
(
    id        bigint primary key auto_increment,
    userId    varchar(100) not null,
    title     varchar(100) not null,
    contents  varchar(100) not null,
    createdAt timestamp    not null,
    deleted   tinyint      not null default 0,
    userName  varchar(100) not null
);

create table reply
(
    id        bigint primary key auto_increment,
    contents  varchar(100) not null,
    createdAt timestamp    not null,
    deleted   tinyint      not null default 0,
    articleId bigint,
    userId    varchar(100),
    userName  varchar(100)
);