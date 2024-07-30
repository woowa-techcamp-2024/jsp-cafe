import org.apache.tomcat.util.json.JSONParser;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONParserTest {

    @Test
    void testParseObject() throws Exception {
        String jsonString = """
                {
                    "name": "John",
                    "age": 30
                }
                """;
        JSONParser parser = new JSONParser(jsonString);
        LinkedHashMap<String, Object> parsedObject = parser.parseObject();
        assertEquals("John", parsedObject.get("name"));
        assertEquals(30, ((BigInteger) parsedObject.get("age")).intValue());
    }
}
