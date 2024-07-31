package com.wootecam.jspcafe.servlet.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpBodyParser {

    private static final int KEY_VALUE_LENGTH = 2;

    private HttpBodyParser() {
    }

    public static Map<String, String> parse(final String bodyData) {
        String decodedData = URLDecoder.decode(bodyData, StandardCharsets.UTF_8);

        return Arrays.stream(decodedData.split("&"))
                .map(param -> param.split("="))
                .filter(params -> params.length == KEY_VALUE_LENGTH)
                .collect(Collectors.toMap(splitParams -> splitParams[0].trim(), splitParams -> splitParams[1].trim()));
    }
}
