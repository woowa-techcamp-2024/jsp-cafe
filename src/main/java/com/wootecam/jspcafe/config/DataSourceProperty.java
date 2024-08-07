package com.wootecam.jspcafe.config;

import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceProperty {

    private static final Logger log = LoggerFactory.getLogger(DataSourceProperty.class);

    private final Properties properties = new Properties();

    public DataSourceProperty(final String profileKey) {
        String profile = System.getenv(profileKey);

        try {
            if (profile == null) {
                properties.load(getClass().getClassLoader().getResourceAsStream("config/datasource.properties"));
                profile = properties.getProperty("profile");
            }
            log.info("PROFILE='{}' is Loading", profile);
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
