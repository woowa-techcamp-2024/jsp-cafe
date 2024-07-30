package com.wootecam.jspcafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wootecam.jspcafe.domain.Question;
import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.exception.BadRequestException;
import com.wootecam.jspcafe.exception.NotFoundException;
import com.wootecam.jspcafe.service.fixture.ServiceTest;
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

class QuestionServiceTest extends ServiceTest {

    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        questionService = new QuestionService(questionRepository);
    }

    @Nested
    class append_메소드는 {

        @Nested
        class 만약_질문_정보가_하나라도_비어있거나_null을_갖는다면 {

            @ParameterizedTest
            @MethodSource("generateInvalidQuestionInfo")
            void 예외를_발생시킨다(List<Object> invalidUserInfo) {
                // given
                userRepository.save(new User("userId", "password", "name", "email"));

                // expect
                assertThatThrownBy(
                        () -> questionService.append((String) invalidUserInfo.get(0), (String) invalidUserInfo.get(1),
                                (String) invalidUserInfo.get(2), (Long) invalidUserInfo.get(3)))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("질문 작성 시 모든 정보를 입력해야 합니다.");
            }

            private static Stream<Arguments> generateInvalidQuestionInfo() {
                return Stream.of(
                        Arguments.of(List.of("", "제목입니다.", "내용입니다.", 1L)),
                        Arguments.of(List.of("작성자", "", "내용입니다.", 1L)),
                        Arguments.of(List.of("작성자", "제목입니다.", "", 1L)),
                        Arguments.of(Arrays.asList(null, "제목입니다.", "내용입니다.", 1L)),
                        Arguments.of(Arrays.asList("작성자", null, "내용입니다.", 1L)),
                        Arguments.of(Arrays.asList("작성자", "제목입니다.", null, 1L)),
                        Arguments.of(Arrays.asList("작성자", "제목입니다.", "내용입니다.", null))
                );
            }
        }

        @Nested
        class 정상적인_질문_입력이라면 {

            @Test
            void 질문을_저장합니다() {
                // given
                userRepository.save(new User("userId", "password", "name", "email"));

                // expect
                assertThatNoException()
                        .isThrownBy(() -> questionService.append("작성자", "제목입니다.", "내용입니다.", 1L));
            }
        }
    }

    @Nested
    class readAll_메소드는 {

        @Test
        void 저장되어있는_모든_질문을_반환한다() {
            // given
            userRepository.save(new User("userId", "password", "name", "email"));
            questionRepository.save(new Question("작성자", "제목입니다.", "내용입니다.", LocalDateTime.now(), 1L));
            questionRepository.save(new Question("작성자", "제목입니다.", "내용입니다.", LocalDateTime.now(), 1L));
            questionRepository.save(new Question("작성자", "제목입니다.", "내용입니다.", LocalDateTime.now(), 1L));

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
                userRepository.save(new User("userId", "password", "name", "email"));
                questionRepository.save(new Question("작성자", "제목입니다.", "내용입니다.", LocalDateTime.now(), 1L));

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

    @Nested
    class readQuestionToEdit_메소드는 {

        @Nested
        class 만약_questionId에_해당하는_질문이_있고_작성자와_사용자가_동일하면 {

            @Test
            void questionId에_해당하는_질문을_반환한다() {
                // given
                userRepository.save(new User("userId", "password", "name", "email"));
                questionRepository.save(new Question("작성자1", "1제목입니다.", "1내용입니다.", LocalDateTime.now(), 1L));
                questionRepository.save(new Question("작성자2", "2제목입니다.", "2내용입니다.", LocalDateTime.now(), 1L));

                // when
                Question question = questionService.readQuestionToEdit(2L, 1L);

                // then
                assertAll(
                        () -> assertThat(question.getId()).isEqualTo(2L),
                        () -> assertThat(question.getWriter()).isEqualTo("작성자2"),
                        () -> assertThat(question.getTitle()).isEqualTo("2제목입니다."),
                        () -> assertThat(question.getContents()).isEqualTo("2내용입니다."),
                        () -> assertThat(question.getUserPrimaryId()).isEqualTo(1L)
                );

            }
        }

        @Nested
        class 만약_questionId값이_null이라면 {

            @Test
            void 예외가_발생한다() {
                // expect
                assertThatThrownBy(() -> questionService.readQuestionToEdit(null, 1L))
                        .isInstanceOf(NotFoundException.class)
                        .hasMessage("수정 할 질문을 찾을 수 없습니다.");
            }
        }

        @Nested
        class 만약_작성자의_id와_로그인한_사용자의_id가_다르면 {

            @Test
            void 예외가_발생한다() {
                // given
                userRepository.save(new User("userId", "password", "name", "email"));
                questionRepository.save(new Question("작성자", "제목입니다.", "내용입니다.", LocalDateTime.now(), 1L));

                // expect
                assertThatThrownBy(() -> questionService.readQuestionToEdit(1L, 2L))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("다른 사용자의 질문은 수정할 수 없습니다.");
            }
        }
    }

    @Nested
    class edit_메소드는 {

        @Nested
        class 만약_정상적인_질문의_제목과_내용이_요청으로_온다면 {

            @Test
            void 게시글의_제목과_내용을_수정한다() {
                // given
                userRepository.save(new User("userId", "password", "name", "email"));
                questionRepository.save(new Question("작성자", "제목입니다.", "내용입니다.", LocalDateTime.now(), 1L));

                // when
                questionService.edit(1L, "변경된 제목입니다.", "변경된 내용입니다.");
                Question question = questionService.read(1L);

                // then
                assertAll(
                        () -> assertThat(question.getTitle()).isEqualTo("변경된 제목입니다."),
                        () -> assertThat(question.getContents()).isEqualTo("변경된 내용입니다.")
                );
            }
        }

        @Nested
        class 만약_수정할_질문의_제목_혹은_내용이_null이라면 {

            @ParameterizedTest
            @MethodSource("generateInvalidEditedQuestionInfo")
            void 예외가_발생한다(String editTitle, String editContents) {
                // given
                userRepository.save(new User("userId", "password", "name", "email"));
                questionRepository.save(new Question("작성자", "제목입니다.", "내용입니다.", LocalDateTime.now(), 1L));

                // expect
                assertThatThrownBy(() -> questionService.edit(1L, editTitle, editContents))
                        .isExactlyInstanceOf(BadRequestException.class)
                        .hasMessage("질문 수정 시 제목과 내용을 모두 입력해야 합니다.");
            }

            private static Stream<Arguments> generateInvalidEditedQuestionInfo() {
                return Stream.of(
                        Arguments.of(null, "내용"),
                        Arguments.of("제목", null),
                        Arguments.of("", "내용"),
                        Arguments.of("제목", "")
                );
            }
        }
    }
}
