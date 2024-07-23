drop table if exists user;
drop table if exists article;

create table user
(
    id       varchar(100) primary key,
    password varchar(100) not null,
    name     varchar(100) not null,
    email    varchar(100) not null
);

create table article
(
    id       bigint primary key auto_increment,
    writer   varchar(100) not null,
    title    varchar(100) not null,
    contents varchar(100) not null
)