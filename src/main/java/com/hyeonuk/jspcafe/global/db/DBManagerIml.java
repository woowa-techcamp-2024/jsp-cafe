package com.hyeonuk.jspcafe.global.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;

public class DBManagerIml implements DBManager {
    private final DBConnectionInfo connectionInfo;
    public DBManagerIml(DBConnectionInfo connectionInfo) throws ClassNotFoundException, SQLException {
        this.connectionInfo = connectionInfo;
        Class.forName(connectionInfo.getDriverClassName());
        try(Connection conn = getConnection()) {
            executeSqlScript(conn,"init.sql");
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionInfo.getUrl(),connectionInfo.getUsername(),connectionInfo.getPassword());
    }
    private String readSqlFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = DBManagerIml.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error reading SQL file",e);
        }
        return sb.toString();
    }

    private void executeSqlScript(Connection conn,String scriptName) throws SQLException {
        String[] sqls = readSqlFile(scriptName).split(";");

        for (String sql : sqls) {
            try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
                if (!sql.trim().isEmpty()) {
                    pstmt.execute();
                }
            }
        }
    }
}
