package cafe.domain.db;

import cafe.domain.util.DatabaseConnector;
import cafe.domain.entity.User;

public class UserDatabase implements Database<String, User> {
    DatabaseConnector databaseConnector;

    public UserDatabase(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public DatabaseConnector getConnector() {
        return this.databaseConnector;
    }
}
