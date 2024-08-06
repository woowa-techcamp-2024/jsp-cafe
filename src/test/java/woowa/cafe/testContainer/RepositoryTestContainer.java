package woowa.cafe.testContainer;

import org.testcontainers.containers.MySQLContainer;

public class RepositoryTestContainer {

    public static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("woowa")
            .withUsername("user1")
            .withPassword("test");

    static {
        mysql.start();
    }
}
