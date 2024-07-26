package cafe.users.repository;

import cafe.database.ConnectionPool;
import cafe.database.JdbcConnectionPool;

public class JdbcUserRepositoryTest extends UserRepositoryTest {
    private final ConnectionPool connectionPool = new JdbcConnectionPool("jdbc:mysql://localhost:3306/test", "root", "password", 5);

    @Override
    protected UserRepository createUserRepository() {
        return new JdbcUserRepository(connectionPool);
    }
}
