package woopaca.jspcafe.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MySQLProperties {

    private static final String url;
    private static final String username;
    private static final String password;

    static {
        Properties properties = new Properties();
        try (InputStream inputStream = MySQLProperties.class.getResourceAsStream("/mysql.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        properties.computeIfAbsent("db.url", key -> System.getenv("CHANWOO_CAFE_DB_URL"));
        properties.computeIfAbsent("db.username", key -> System.getenv("CHANWOO_CAFE_DB_USERNAME"));
        properties.computeIfAbsent("db.password", key -> System.getenv("CHANWOO_CAFE_DB_PASSWORD"));
        url = properties.getProperty("db.url");
        username = properties.getProperty("db.username");
        password = properties.getProperty("db.password");
    }

    private MySQLProperties() {
    }

    public static String getUrl() {
        return url;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }
}
