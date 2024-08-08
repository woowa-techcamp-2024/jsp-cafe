create table IF NOT EXISTS users
(
    id       bigint auto_increment
        primary key,
    user_id  varchar(100) null,
    username varchar(100) null,
    email    varchar(100) null,
    password varchar(255) null,
    created_at timestamp default CURRENT_TIMESTAMP null,
    updated_date timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint idx_user_id
        unique (user_id)
);


CREATE TABLE IF NOT EXISTS replies
(
    id         bigint auto_increment
        primary key,
    article_id bigint                              not null,
    user_id    bigint                              not null,
    content    text                                not null,
    created_at timestamp default CURRENT_TIMESTAMP null,
    deleted_at timestamp                           null
);


CREATE TABLE IF NOT EXISTS articles
(
    id           bigint auto_increment
        primary key,
    user_id      bigint                              null,
    title        varchar(255)                        null,
    content      text                                null,
    created_date timestamp default CURRENT_TIMESTAMP null,
    updated_date timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    deleted_at   timestamp                           null
);

CREATE INDEX idx_articles_active
    on articles (((`deleted_at` is null)) asc, id desc);

CREATE INDEX idx_replies_article_id_user_id_id ON replies (article_id, user_id);
CREATE INDEX idx_replies_article_id ON replies (article_id);