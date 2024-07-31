package com.wootecam.jspcafe.servlet.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class HttpBodyParserTest {

    @Test
    void 바디데이터에서_파라미터를_파싱할_수_있다() {
        // given
        String bodyData = "title=%EB%B0%98%EA%B0%91%EC%8A%B5%EB%8B%88%EB%8B%A4!&contents=%EC%95%88%EB%85%95%ED%95%98%EC%84%B8%EC%9A%94!";

        // when
        Map<String, String> parse = HttpBodyParser.parse(bodyData);

        // then
        assertThat(parse)
                .containsExactlyInAnyOrderEntriesOf(Map.of("title", "반갑습니다!", "contents", "안녕하세요!"));
    }

    @Test
    void value가_없다면_null로_파싱한다() {
        // given
        String bodyData = "title=%EB%B0%98%EA%B0%91%EC%8A%B5%EB%8B%88%EB%8B%A4!&contents=";

        // when
        String contents = HttpBodyParser.parse(bodyData)
                .get("contents");

        // then
        assertThat(contents).isNull();
    }
}
