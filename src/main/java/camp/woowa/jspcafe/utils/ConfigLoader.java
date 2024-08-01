package camp.woowa.jspcafe.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private ConfigLoader() {}

    public static Properties loadConfig() {
        Properties props = new Properties();
        try (InputStream in = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("config.props 파일을 읽을 수 없습니다.", e);
        }

        return props;
    }
}
