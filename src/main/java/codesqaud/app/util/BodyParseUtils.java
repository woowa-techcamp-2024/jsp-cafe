package codesqaud.app.util;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BodyParseUtils {
    public static Map<String, String> parseBody(HttpServletRequest request) throws IOException {
        String body = readBody(request);

        Map<String, String> parameters = new HashMap<>();

        String[] elements = body.split("&");
        for (String element : elements) {
            String[] keyValue = element.split("=");
            if(keyValue.length < 2) {
                continue;
            }
            String key = decode(keyValue[0]);
            String value = decode(keyValue[1]);

            parameters.put(key, value);
        }

        return parameters;
    }

    private static String readBody(HttpServletRequest request) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();

        while (bufferedReader.ready()) {
            stringBuilder.append(bufferedReader.readLine());
        }
        return stringBuilder.toString();
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
