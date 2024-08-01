package camp.woowa.jspcafe.repository;

import java.sql.Connection;

public class RepositoryFactory {
    private RepositoryFactory() {}

    public static UserRepository createUserRepository(Connection conn) {
        return new MySQLUserRepository(conn);
    }

    public static ReplyRepository createReplyRepository(Connection conn) {
        return new MySQLReplyRepository(conn);
    }

    public static QuestionRepository createQuestionRepository(Connection conn) {
        return new MySQLQuestionRepository(conn);
    }
}
