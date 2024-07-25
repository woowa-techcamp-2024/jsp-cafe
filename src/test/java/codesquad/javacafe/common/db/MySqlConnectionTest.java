package codesquad.javacafe.common.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MySqlConnectionTest {

    @Test
    @DisplayName("MySQL Connection Test")
    void testConnection() {
        MySqlConnection.getConnection();

    }
}