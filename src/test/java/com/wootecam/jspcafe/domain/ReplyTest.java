package com.wootecam.jspcafe.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wootecam.jspcafe.exception.BadRequestException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReplyTest {

    @ParameterizedTest
    @MethodSource("generateInvalidReplyInfo")
    void 댓글을_작성할때_정보_중_하나라도_비어있거나_null이라면_예외가_발생한다(List<Object> invalidReplyInfo) {
        // expect
        assertThatThrownBy(() -> new Reply((String) invalidReplyInfo.get(0), (String) invalidReplyInfo.get(1),
                (LocalDateTime) invalidReplyInfo.get(2), (Long) invalidReplyInfo.get(3),
                (Long) invalidReplyInfo.get(4)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("댓글 작성시 모든 정보를 입력해야 합니다.");
    }

    private static Stream<Arguments> generateInvalidReplyInfo() {
        LocalDateTime now = LocalDateTime.now();

        return Stream.of(
                Arguments.of(List.of("", "contents", now, 1L, 1L)),
                Arguments.of(List.of("writer", "", now, 1L, 1L)),
                Arguments.of(Arrays.asList(null, "contents", now, 1L, 1L)),
                Arguments.of(Arrays.asList("writer", null, now, 1L, 1L)),
                Arguments.of(Arrays.asList("writer", "contents", null, 1L, 1L)),
                Arguments.of(Arrays.asList("writer", "contents", now, null, 1L)),
                Arguments.of(Arrays.asList("writer", "contents", now, 1L, null))
        );
    }
}
