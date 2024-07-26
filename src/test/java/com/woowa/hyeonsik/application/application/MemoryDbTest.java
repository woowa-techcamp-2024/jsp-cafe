package com.woowa.hyeonsik.application.application;

import com.woowa.hyeonsik.server.database.DatabaseConnector;
import com.woowa.hyeonsik.server.database.property.H2Property;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class MemoryDbTest {
    private static final DatabaseConnector connector = new DatabaseConnector(new H2Property());
    private static final DatabaseInitializer databaseInitializer = new DatabaseInitializer(connector);
    private static final DatabaseCleanup databaseCleanup = new DatabaseCleanup(connector);

    @BeforeAll
    static void init() {
        databaseInitializer.init();
    }

    @BeforeEach
    void clean() {
        databaseCleanup.clean();
    }
}
