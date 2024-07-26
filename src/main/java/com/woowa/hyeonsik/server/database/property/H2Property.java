package com.woowa.hyeonsik.server.database.property;

public final class H2Property implements DatabaseProperty {
    private final String driverName = "org.h2.Driver";
    private final String url = "jdbc:h2:mem:was;DB_CLOSE_DELAY=-1";
    private final String user = "sa";
    private final String password = "";

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
