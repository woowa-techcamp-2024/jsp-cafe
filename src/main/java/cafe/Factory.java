package cafe;

import cafe.database.ConnectionPool;
import cafe.database.RealJdbcConnectionPool;
import cafe.questions.repository.ArticleRepository;
import cafe.questions.repository.JdbcArticleRepository;
import cafe.questions.repository.JdbcReplyRepository;
import cafe.questions.repository.ReplyRepository;
import cafe.users.repository.JdbcUserRepository;
import cafe.users.repository.UserRepository;
import cafe.util.PropertiesLoader;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class Factory {
    private final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();
    private final Map<Class<?>, Object> locks = new ConcurrentHashMap<>();
    private final PropertiesLoader dbProperties = new PropertiesLoader("db.properties");

    public UserRepository userRepository() {
        return getOrCreate(UserRepository.class, () -> new JdbcUserRepository(connectionPool()));
    }

    public ArticleRepository articleRepository() {
        return getOrCreate(ArticleRepository.class, () -> new JdbcArticleRepository(connectionPool()));
    }

    public ReplyRepository replyRepository() {
        return getOrCreate(ReplyRepository.class, () -> new JdbcReplyRepository(connectionPool()));
    }

    public ConnectionPool connectionPool() {
        return getOrCreate(ConnectionPool.class, () -> new RealJdbcConnectionPool(mysqlConnectionPoolDataSource()));
    }

    public MysqlConnectionPoolDataSource mysqlConnectionPoolDataSource() {
        return getOrCreate(MysqlConnectionPoolDataSource.class, () -> {
            MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
            dataSource.setUrl(dbProperties.getProperty("jdbc.url"));
            dataSource.setUser(dbProperties.getProperty("jdbc.user"));
            dataSource.setPassword(dbProperties.getProperty("jdbc.password"));
            return dataSource;
        });
    }

    protected <T> T getOrCreate(Class<T> beanClass, Supplier<T> supplier) {
        Object lock = locks.computeIfAbsent(beanClass, k -> new Object());
        synchronized (lock) {
            if (!instances.containsKey(beanClass)) {
                T instance = supplier.get();
                instances.put(beanClass, instance);
                return instance;
            } else {
                return beanClass.cast(instances.get(beanClass));
            }
        }
    }
}
