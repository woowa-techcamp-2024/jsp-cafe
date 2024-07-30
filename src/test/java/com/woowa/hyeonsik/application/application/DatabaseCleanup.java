package com.woowa.hyeonsik.application.application;

import com.woowa.hyeonsik.server.database.DatabaseConnector;
import java.util.List;

public class DatabaseCleanup {
    private final DatabaseConnector databaseConnector;

    public DatabaseCleanup(final DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public void clean() {
        List.of(
            "TRUNCATE TABLE member",
            "TRUNCATE TABLE article",
            "TRUNCATE TABLE comment",
            "ALTER TABLE article ALTER COLUMN article_id RESTART WITH 1",
            "ALTER TABLE comment ALTER COLUMN id RESTART WITH 1"
        ).forEach(databaseConnector::execute);
    }
}
