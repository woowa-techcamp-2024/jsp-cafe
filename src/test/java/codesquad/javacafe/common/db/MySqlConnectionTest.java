package codesquad.javacafe.common.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class MySqlConnectionTest {

    @Test
    @DisplayName("MySQL Connection Test")
    void testConnection() {
        DBConnection.getConnection();
    }
}