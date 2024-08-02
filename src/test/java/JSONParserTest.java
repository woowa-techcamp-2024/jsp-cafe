import com.google.gson.Gson;
import org.apache.tomcat.util.json.JSONParser;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONParserTest {

    @Test
    @Disabled("Tomcat JSONParser does not support escape characters")
    void testParseObject() throws Exception {
        String jsonString = """
                {
                    "name": "John \\n Doe",
                    "age": 30
                }
                """;
        JSONParser parser = new JSONParser(jsonString);
        LinkedHashMap<String, Object> tomcatParsed = parser.parseObject();
        String tomcatName = (String) tomcatParsed.get("name");
        System.out.println(tomcatName);
        Gson gson = new Gson();
        LinkedHashMap gsonParsed = gson.fromJson(jsonString, LinkedHashMap.class);
        String gsonName = (String) gsonParsed.get("name");
        System.out.println(gsonName);

        assertEquals(tomcatName, gsonName);
    }
}
