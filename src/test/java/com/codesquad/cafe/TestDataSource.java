package com.codesquad.cafe;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.Logger;

public class TestDataSource {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(TestDataSource.class);

    public static DataSource dataSource() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:cafe;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        ds.setPassword("");
        return ds;
    }

    public static void createTable(DataSource ds) {
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {
            // init.sql 파일 읽기
            InputStream inputStream = TestDataSource.class.getResourceAsStream("/init.sql");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String sql = sb.toString();
            for (String sqlStatement : sql.split(";")) {
                if (!sqlStatement.trim().isEmpty()) {
                    stmt.execute(sqlStatement);
                }
            }
            log.info("SQL executed: {}", sql);
        } catch (Exception e) {
            log.warn("Fail to init table: {}", e);
        }
    }

}
