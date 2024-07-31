package cafe.domain.db;

import cafe.domain.util.DatabaseConnector;
import cafe.domain.entity.Article;

public class ArticleDatabase implements Database<String, Article> {
    DatabaseConnector databaseConnector;

    public ArticleDatabase(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public DatabaseConnector getConnector() {
        return databaseConnector;
    }
}
