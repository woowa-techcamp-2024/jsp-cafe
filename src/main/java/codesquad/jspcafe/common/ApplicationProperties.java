package codesquad.jspcafe.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {

    private final Properties properties;

    public ApplicationProperties() {
        this.properties = init();
    }

    public String getJdbcUrl() {
        return properties.getProperty("jdbc.mysql.url");
    }

    public String getJdbcUsername() {
        return properties.getProperty("jdbc.mysql.username");
    }

    public String getJdbcPassword() {
        return properties.getProperty("jdbc.mysql.password");
    }

    private Properties init() {
        Properties prop = new Properties();
        try (InputStream inputStream = getClass().getResourceAsStream("/application.properties")) {
            prop.load(inputStream);
        } catch (IOException ignore) {
        }
        return prop;
    }
}
