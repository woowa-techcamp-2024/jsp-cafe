package com.wootecam.jspcafe.config;

import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceProperty {

    private static final Logger log = LoggerFactory.getLogger(DataSourceProperty.class);

    private final Properties properties = new Properties();

    public DataSourceProperty() {
        loadProperties();
    }

    private void loadProperties() {
        try {
            String profile = System.getenv("PROFILE");
            if (profile == null) {
                properties.load(getClass().getClassLoader().getResourceAsStream("config/datasource.properties"));
                properties.load(
                        getClass().getClassLoader().getResourceAsStream("config/" + properties.getProperty("profile")));
                return;
            }
            properties.load(getClass().getClassLoader().getResourceAsStream("config/" + profile));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String getProperty(final String key) {
        return properties.getProperty(key);
    }

    public String getDriverClassName() {
        return getProperty("datasource.driverClassName");
    }

    public String getUrl() {
        return getProperty("datasource.url");
    }

    public String getUser() {
        return getProperty("datasource.user");
    }

    public String getPassword() {
        return getProperty("datasource.password");
    }
}
