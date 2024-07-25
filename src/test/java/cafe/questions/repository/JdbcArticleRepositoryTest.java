package cafe.questions.repository;

import cafe.database.ConnectionPool;
import cafe.database.JdbcConnectionPool;

class JdbcArticleRepositoryTest extends ArticleRepositoryTest {
    private final ConnectionPool connectionPool = new JdbcConnectionPool("jdbc:mysql://localhost:3306/test", "root", "password", 5);

    @Override
    protected ArticleRepository createArticleRepository() {
        return new JdbcArticleRepository(connectionPool);
    }
}