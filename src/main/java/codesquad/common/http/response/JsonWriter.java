package codesquad.common.http.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public final class JsonWriter {
    private JsonWriter() {
    }

    public static void writeJson(HttpServletResponse resp, Object returnValue) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String responseStr = objectMapper.writeValueAsString(returnValue);
        objectMapper.writeValue(resp.getWriter(), responseStr);
    }
}
