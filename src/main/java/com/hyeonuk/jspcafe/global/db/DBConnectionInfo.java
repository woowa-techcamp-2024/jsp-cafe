package com.hyeonuk.jspcafe.global.db;

import com.hyeonuk.jspcafe.utils.Yaml;
import com.hyeonuk.jspcafe.utils.YamlParser;

public class DBConnectionInfo {
    private final String url;
    private final String username;
    private final String password;
    private final String driverClassName;

    public DBConnectionInfo(String url, String username, String password, String driverClassName) {
        validation(url,username,password,driverClassName);
        this.url = url;
        this.username = username;
        this.password = password;
        this.driverClassName = driverClassName;
    }

    public DBConnectionInfo(String yamlPath) {
        YamlParser yamlParser = new YamlParser();
        Yaml yaml = yamlParser.parse(yamlPath);

        String url = yaml.getString("db.datasource.url");
        String username = yaml.getString("db.datasource.username");
        String password = yaml.getString("db.datasource.password");
        String driverClassName = yaml.getString("db.datasource.driver-class-name");
        validation(url,username,password,driverClassName);
        this.url = url;
        this.username = username;
        this.password = password;
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    private void validation(String url, String username, String password, String driverClassName){
        if(url == null || username == null || password == null || driverClassName == null){
            throw new IllegalArgumentException("url, username, password and driverClassName must not be null");
        }
    }
}
