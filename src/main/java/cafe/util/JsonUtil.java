package cafe.util;

import java.io.BufferedReader;
import java.io.IOException;

public class JsonUtil {
    private JsonUtil() {
    }

    public static String readJson(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
