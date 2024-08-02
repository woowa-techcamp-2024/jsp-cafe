package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class DatabaseManagerTest {

    @Test
    void getConnection() {
        try (Connection conn = DatabaseManager.getConnection()) {
            assertNotNull(conn, "Connection should not be null");
        } catch (SQLException e) {
            fail("SQLException should not be thrown");
        }
    }

    @Test
    void executeSqlScript() {
        String script = "CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY, name VARCHAR(50));" +
            "INSERT INTO test_table (id, name) VALUES (1, 'test');";

        DatabaseManager.executeSqlScript(script);

        try (Connection conn = DatabaseManager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM test_table WHERE id = 1")) {

            assertTrue(rs.next(), "ResultSet should have at least one row");
            assertEquals(1, rs.getInt("id"), "ID should be 1");
            assertEquals("test", rs.getString("name"), "Name should be 'test'");

        } catch (SQLException e) {
            fail("SQLException should not be thrown");
        }
    }
}