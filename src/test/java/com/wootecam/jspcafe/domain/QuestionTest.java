package com.wootecam.jspcafe.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wootecam.jspcafe.exception.BadRequestException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class QuestionTest {

    @ParameterizedTest
    @MethodSource("generateInvalidQuestionInfo")
    void 질문_폼_입력에서_null혹은_빈칸이_하나라도_입력되면_예외가_발생한다(List<Object> invalidQuestionInfo) {
        // expect
        assertThatThrownBy(() -> new Question((String) invalidQuestionInfo.get(0), (String) invalidQuestionInfo.get(1),
                (String) invalidQuestionInfo.get(2), (LocalDateTime) invalidQuestionInfo.get(3),
                (Long) invalidQuestionInfo.get(4)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("질문 작성 시 모든 정보를 입력해야 합니다.");
    }

    private static Stream<Arguments> generateInvalidQuestionInfo() {
        return Stream.of(
                Arguments.of(List.of("", "제목입니다.", "내용입니다.", LocalDateTime.now(), 1L)),
                Arguments.of(List.of("작성자", "", "내용입니다.", LocalDateTime.now(), 1L)),
                Arguments.of(List.of("작성자", "제목입니다.", "", LocalDateTime.now(), 1L)),
                Arguments.of(Arrays.asList(null, "제목입니다.", "내용입니다.", LocalDateTime.now(), 1L)),
                Arguments.of(Arrays.asList("작성자", null, "내용입니다.", LocalDateTime.now(), 1L)),
                Arguments.of(Arrays.asList("작성자", "제목입니다.", null, LocalDateTime.now(), 1L)),
                Arguments.of(Arrays.asList("작성자", "제목입니다.", "내용입니다.", null, 1L)),
                Arguments.of(Arrays.asList("작성자", "제목입니다.", "내용입니다.", LocalDateTime.now(), null))
        );
    }

    @Nested
    class isSameWriter_메소드는 {

        @Test
        void 동일한_사용자의_id가_인자로오면_true를_반환한다() {
            // given
            Question question = new Question(1L, "작성자", "제목", "내용", LocalDateTime.now(), 1L);

            // expect
            assertThat(question.isSameWriter(1L)).isTrue();
        }

        @Test
        void 다른_사용자의_id가_인자로오면_false를_반환한다() {
            // given
            Question question = new Question(1L, "작성자", "제목", "내용", LocalDateTime.now(), 1L);

            // expect
            assertThat(question.isSameWriter(2L)).isFalse();
        }
    }
}
