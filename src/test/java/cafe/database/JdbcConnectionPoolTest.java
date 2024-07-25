package cafe.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class JdbcConnectionPoolTest {

    private JdbcConnectionPool pool;

    @BeforeEach
    public void setUp() {
        // Initialize the pool with example parameters
        pool = new JdbcConnectionPool("jdbc:mysql://localhost:3306/test", "root", "password", 1);
    }

    @AfterEach
    public void tearDown() {
        // Clean up resources
        pool.shutdown();
    }

    @Test
    public void pool의_Connection은_재사용가능하다() throws SQLException {
        Connection connection1 = pool.getConnection();
        assertNotNull(connection1);
        connection1.close();
        Connection connection2 = pool.getConnection();
        assertNotNull(connection2);
        connection2.close();
        assertEquals(connection1, connection2, "Connections should be reused from the pool");
    }

    @Test
    public void connection이_비정상적으로닫혀도_복구된다() throws SQLException {
        PooledConnection connection1 = (PooledConnection) pool.getConnection();
        assertNotNull(connection1);

        // Simulate unexpected connection close
        pool.releaseConnection(connection1);
        connection1.getOriginalConnection().close();

        // Try to get another connection
        PooledConnection connection2 = (PooledConnection) pool.getConnection();
        assertNotNull(connection2);
        assertFalse(connection2.getOriginalConnection().isClosed(), "New connection should be created if the old one is closed");
        assertNotEquals(connection1, connection2, "New connection should be created if the old one is closed");

        connection2.close();
    }
}