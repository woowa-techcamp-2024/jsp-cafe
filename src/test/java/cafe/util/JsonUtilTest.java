package cafe.util;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilTest {

    @Test
    void 올바른_입력을_받아_출력한다() throws IOException {
        // given
        String expected = "{\"key\":\"value\"}";
        String input = "{\n\"key\":\"value\"\n}";
        BufferedReader reader = new BufferedReader(new java.io.StringReader(input));

        // when
        String actual = JsonUtil.readJson(reader);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void 인자가_null이면_예외가_발생한다() {
        // given, when, then
        assertThrows(NullPointerException.class, () -> JsonUtil.readJson(null));
    }
}