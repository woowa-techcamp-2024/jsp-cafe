package org.example.demo.repository;

import org.example.demo.db.DbConfig;

public class RepositoryTestSupport {
    DbConfig dbConfig;

    void setDb() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String jdbcUrl = "jdbc:h2:mem:~/cafe;MODE=MySQL";
        String user = "sa";
        String password = "";

        dbConfig = new DbConfig(jdbcUrl, user, password);
        dbConfig.initializeSchema();
    }

    void cleanUp() {
        try (var connection = dbConfig.getConnection();
             var statement = connection.createStatement()) {
            statement.execute("SET REFERENTIAL_INTEGRITY FALSE");
            statement.execute("TRUNCATE TABLE posts");
            statement.execute("TRUNCATE TABLE `users`");
            statement.execute("TRUNCATE TABLE comments");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
