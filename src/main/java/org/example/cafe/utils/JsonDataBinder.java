package org.example.cafe.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;

public final class JsonDataBinder {

    private JsonDataBinder() {
    }

    public static <T> T bind(ObjectMapper objectMapper, InputStream inputStream, Class<T> type)
            throws IOException {
        return objectMapper.readValue(inputStream, type);
    }
}
