package cafe.domain.db;

import cafe.domain.entity.Comment;
import cafe.domain.util.DatabaseConnector;

public class CommentDatabase implements Database<String, Comment> {
    private final DatabaseConnector databaseConnector;

    public CommentDatabase(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public DatabaseConnector getConnector() {
        return databaseConnector;
    }
}
