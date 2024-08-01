package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.db.DatabaseManager;

import javax.sql.DataSource;

public class RepositoryFactory {
    private RepositoryFactory() {}

    public static UserRepository createUserRepository(DatabaseManager dm) {
        return new MySQLUserRepository(dm);
    }

    public static ReplyRepository createReplyRepository(DatabaseManager dm) {
        return new MySQLReplyRepository(dm);
    }

    public static QuestionRepository createQuestionRepository(DatabaseManager dm) {
        return new MySQLQuestionRepository(dm);
    }
}
