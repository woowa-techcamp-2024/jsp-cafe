package com.hyeonuk.jspcafe.global.db.mysql;

import com.hyeonuk.jspcafe.global.db.DBManager;
import com.hyeonuk.jspcafe.utils.Yaml;
import com.hyeonuk.jspcafe.utils.YamlParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;

public class MysqlManager implements DBManager {
    private final String url;
    private final String username;
    private final String password;
    public MysqlManager() throws ClassNotFoundException, SQLException {
        YamlParser yamlParser = new YamlParser();
        Yaml yaml = yamlParser.parse("application-db.yml");

        url = yaml.getString("db.datasource.url");
        username = yaml.getString("db.datasource.username");
        password = yaml.getString("db.datasource.password");
        String driver = yaml.getString("db.datasource.driver-class-name");

        Class.forName(driver);
        try(Connection conn = getConnection()) {
            executeSqlScript(conn,"init.sql");
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,username,password);
    }
    private String readSqlFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = MysqlManager.class.getClassLoader().getResourceAsStream(fileName);
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
                    pstmt.execute(sql);
                }
            }
        }
    }
}
