package woowa.frame.core.mapper;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class JsonMapperTest {

    JsonMapper jsonMapper = new JsonMapper();

    @Test
    public void parseMap() {
        // given
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> innerMap = new HashMap<>();
        map.put("key", "value");
        map.put("key2", innerMap);
        innerMap.put("key3", "value3");

        // when
        String result = jsonMapper.mapToJson(map);

        // then
        assertThat(result).isEqualTo("{\"key2\":{\"key3\":\"value3\"},\"key\":\"value\"}");
    }
}