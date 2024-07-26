package com.woowa.hyeonsik.server.database.property;

public final class MysqlProperty implements DatabaseProperty {
    private final String driverName = "com.mysql.cj.jdbc.Driver";
    private final String url = "jdbc:mysql://127.0.0.1:3306/woowa";
    private final String user = "root";
    private final String password = "12345678";

    public String getDriverName() {
        return driverName;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
