package woowa.frame.web.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FormParserTest {

    @Test
    @DisplayName("FormParser.parse() 메서드는 form 데이터를 파싱하여 Map으로 반환한다.")
    void parse() {
        // given
        FormParser formParser = new FormParser();
        String form = "writer=writer&title=title&content=content";

        // when
        var result = formParser.parse(form);

        // then
        assertEquals(3, result.size());
        assertEquals("writer", result.get("writer"));
        assertEquals("title", result.get("title"));
        assertEquals("content", result.get("content"));
    }

    @Test
    @DisplayName("존재하지 않은 데이터인 경우 null을 반환한다.")
    void parseWithNull() {
        // given
        FormParser formParser = new FormParser();
        String form = "nocontent=";

        // when
        var result = formParser.parse(form);

        // then
        assertNull(result.get("content"));
        assertNull(result.get("nocontent"));
    }
}