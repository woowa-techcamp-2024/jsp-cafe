package woowa.frame.core.mapper;

import java.util.Map;

public class JsonMapper {

    public String mapToJson(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Object key : map.keySet()) {
            Object value = map.get(key);
            sb.append("\"").append(key).append("\":");
            if (value instanceof Map<?, ?>) {
                value = mapToJson((Map<?, ?>) value);
            } else {
                value = "\"" + value + "\"";
            }
            sb.append(value).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }

}
