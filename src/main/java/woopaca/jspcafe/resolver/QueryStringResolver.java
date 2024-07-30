package woopaca.jspcafe.resolver;

import woopaca.jspcafe.error.BadRequestException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class QueryStringResolver {

    private QueryStringResolver() {
    }

    public static <T> T resolve(String queryString, Class<T> recordClass) {
        if (queryString == null || queryString.isEmpty()) {
            throw new BadRequestException("[ERROR] QueryString이 비어있습니다.");
        }

        Map<String, String[]> queryParameters = new HashMap<>();
        Arrays.stream(queryString.split("&"))
                .forEach(pair -> putValue(pair, queryParameters));
        return RequestParametersResolver.resolve(queryParameters, recordClass);
    }

    private static void putValue(String pair, Map<String, String[]> queryParameters) {
        try {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = URLDecoder.decode(keyValue[1], "UTF-8");
                queryParameters.put(key, new String[]{value});
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
