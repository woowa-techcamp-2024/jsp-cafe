package com.wootecam.jspcafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wootecam.jspcafe.domain.Question;
import com.wootecam.jspcafe.domain.QuestionRepository;
import com.wootecam.jspcafe.repository.InMemoryQuestionRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class QuestionServiceTest {

    private QuestionRepository questionRepository;
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        questionRepository = new InMemoryQuestionRepository();
        questionService = new QuestionService(questionRepository);
    }

    @Nested
    class append_메소드는 {

        @Nested
        class 만약_질문_정보가_하나라도_비어있거나_null을_갖는다면 {

            @ParameterizedTest
            @MethodSource("generateInvalidQuestionInfo")
            void 예외를_발생시킨다(List<String> invalidUserInfo) {
                assertThatThrownBy(() -> questionService.append(invalidUserInfo.get(0), invalidUserInfo.get(1),
                        invalidUserInfo.get(2)))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("질문 작성 시 모든 정보를 입력해야 합니다.");
            }

            private static Stream<Arguments> generateInvalidQuestionInfo() {
                return Stream.of(
                        Arguments.of(List.of("", "제목입니다.", "내용입니다.")),
                        Arguments.of(List.of("작성자", "", "내용입니다.")),
                        Arguments.of(List.of("작성자", "제목입니다.", "")),
                        Arguments.of(Arrays.asList(null, "제목입니다.", "내용입니다.")),
                        Arguments.of(Arrays.asList("작성자", null, "내용입니다.")),
                        Arguments.of(Arrays.asList("작성자", "제목입니다.", null))
                );
            }
        }

        @Nested
        class 정상적인_질문_입력이라면 {

            @Test
            void 질문을_저장합니다() {
                // expect
                assertThatNoException()
                        .isThrownBy(() -> questionService.append("작성자", "제목입니다.", "내용입니다."));
            }
        }
    }

    @Nested
    class readAll_메소드는 {

        @Test
        void 저장되어있는_모든_질문을_반환한다() {
            // given
            questionRepository.save(new Question(1L, "작성자", "제목입니다.", "내용입니다.", LocalDateTime.now()));
            questionRepository.save(new Question(2L, "작성자", "제목입니다.", "내용입니다.", LocalDateTime.now()));
            questionRepository.save(new Question(3L, "작성자", "제목입니다.", "내용입니다.", LocalDateTime.now()));

            // when
            List<Question> questions = questionService.readAll();

            // then
            assertThat(questions).size()
                    .isEqualTo(3);
        }
    }

    @Nested
    class read_메소드는 {

        @Nested
        class 만약_id에_해당하는_질문이_있다면 {

            @Test
            void id에_해당하는_질문을_반환한다() {
                // given
                questionRepository.save(new Question(1L, "작성자", "제목입니다.", "내용입니다.", LocalDateTime.now()));

                // when
                Question question = questionService.read(1L);

                // then
                assertAll(
                        () -> assertThat(question.getId()).isEqualTo(1L),
                        () -> assertThat(question.getWriter()).isEqualTo("작성자"),
                        () -> assertThat(question.getTitle()).isEqualTo("제목입니다."),
                        () -> assertThat(question.getContents()).isEqualTo("내용입니다.")
                );
            }
        }
    }
}
