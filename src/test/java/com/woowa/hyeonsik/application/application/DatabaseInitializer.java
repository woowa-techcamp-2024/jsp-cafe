package com.woowa.hyeonsik.application.application;

import com.woowa.hyeonsik.server.database.DatabaseConnector;
import java.util.List;

public class DatabaseInitializer {
    private final DatabaseConnector databaseConnector;

    public DatabaseInitializer(DatabaseConnector connector) {
        databaseConnector = connector;
    }

    public void init() {
        List.of(
            """
             CREATE TABLE IF NOT EXISTS `member` (
                 `member_id` varchar(255) NOT NULL,
                 `password`  varchar(255) DEFAULT NULL,
                 `name`      varchar(255) DEFAULT NULL,
                 `email`     varchar(255) DEFAULT NULL,
                 PRIMARY KEY (`member_id`)
             );
             """ ,
            """
             CREATE TABLE IF NOT EXISTS `article` (
                 `article_id`  bigint NOT NULL AUTO_INCREMENT,
                 `writer_id`   varchar(255) DEFAULT NULL,
                 `title`       varchar(255) DEFAULT NULL,
                 `contents`    text,
                 `create_at`   timestamp NULL DEFAULT NULL,
                 `modified_at` timestamp NULL DEFAULT NULL,
                 PRIMARY KEY (`article_id`)
             ) 
             """
        ).forEach(databaseConnector::execute);
    }
}
