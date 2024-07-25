package com.wootecam.jspcafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        userService = new UserService(userRepository);
    }

    @Nested
    class signup_메소드는 {

        @Nested
        class 만약_회원가입_정보가_하나라도_비어있거나_null을_갖는다면 {

            @ParameterizedTest
            @MethodSource("generateInvalidUserInfo")
            void 예외를_발생시킨다(List<String> invalidUserInfo) {
                // expect
                assertThatThrownBy(
                        () -> userService.signup(invalidUserInfo.get(0), invalidUserInfo.get(1), invalidUserInfo.get(2),
                                invalidUserInfo.get(3)))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("회원가입 시 모든 정보를 입력해야 합니다.");
            }

            private static Stream<Arguments> generateInvalidUserInfo() {
                return Stream.of(
                        Arguments.of(List.of("", "password", "name", "email")),
                        Arguments.of(List.of("userId", "", "name", "email")),
                        Arguments.of(List.of("userId", "password", "", "email")),
                        Arguments.of(List.of("userId", "password", "name", "")),
                        Arguments.of(Arrays.asList(null, "password", "name", "email")),
                        Arguments.of(Arrays.asList("userId", null, "name", "email")),
                        Arguments.of(Arrays.asList("userId", "password", null, "email")),
                        Arguments.of(Arrays.asList("userId", "password", "name", null))
                );
            }
        }

        @Nested
        class 정상적인_회원가입_정보라면 {

            @Test
            void 사용자가_저장된다() {
                // expect
                assertThatNoException()
                        .isThrownBy(() -> userService.signup("id", "password", "name", "email"));
            }
        }
    }

    @Nested
    class read_메소드는 {

        @Nested
        class 만약_id에_해당되는_사용자를_찾을수_없다면 {

            @Test
            void 예외를_발생시킨다() {
                // expect
                assertThatThrownBy(() -> userService.read(555555L))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("사용자를 찾을 수 없습니다. input path = " + 555555L);
            }
        }

        @Nested
        class id에_해당되는_사용자가_있다면 {

            @Test
            void 해당_사용자를_반환한다() {
                // given
                userRepository.save(new User(1L, "id", "password", "name", "email"));

                // when
                User user = userService.read(1L);

                // then
                assertAll(
                        () -> assertThat(user.getId()).isEqualTo(1L),
                        () -> assertThat(user.getUserId()).isEqualTo("id"),
                        () -> assertThat(user.getPassword()).isEqualTo("password"),
                        () -> assertThat(user.getEmail()).isEqualTo("email")
                );
            }
        }
    }

    @Nested
    class readAll_메소드는 {

        @Test
        void 저장되어있는_모든_사용자를_반환한다() {
            // given
            userRepository.save(new User(1L, "id", "password", "name", "email"));
            userRepository.save(new User(2L, "id", "password", "name", "email"));
            userRepository.save(new User(3L, "id", "password", "name", "email"));

            // when
            List<User> users = userService.readAll();

            // then
            assertThat(users).size()
                    .isEqualTo(3);
        }
    }

    @Nested
    class edit_메소드는 {

        @Nested
        class 정상적인_수정_요청이_들어온다면 {

            @Test
            void 회원정보를_수정한다() {
                // given
                userRepository.save(new User(1L, "userId", "password", "name", "email"));

                // when
                userService.edit(1L, "password", "newPassword", "newName", "newEmail");
                User editedUser = userService.read(1L);

                // then
                assertAll(
                        () -> assertThat(editedUser.getId()).isEqualTo(1L),
                        () -> assertThat(editedUser.getUserId()).isEqualTo("userId"),
                        () -> assertThat(editedUser.getPassword()).isEqualTo("newPassword"),
                        () -> assertThat(editedUser.getName()).isEqualTo("newName"),
                        () -> assertThat(editedUser.getEmail()).isEqualTo("newEmail")
                );
            }
        }

        @Nested
        class 수정하는_사용자의_정보가_하나라도_비어있거나_null이라면 {

            @ParameterizedTest
            @MethodSource("generateInvalidEditUserInfo")
            void 예외가_발생한다(List<String> invalidEditUserInfo) {
                // given
                userRepository.save(new User(1L, "id", "password", "name", "email"));

                // expect
                assertThatThrownBy(
                        () -> userService.edit(1L, invalidEditUserInfo.get(0), invalidEditUserInfo.get(1),
                                invalidEditUserInfo.get(2),
                                invalidEditUserInfo.get(3)))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("회원 수정 시 모든 정보를 입력해야 합니다.");
            }

            private static Stream<Arguments> generateInvalidEditUserInfo() {
                return Stream.of(
                        Arguments.of(List.of("", "newPassword", "name", "email")),
                        Arguments.of(List.of("originalPassword", "", "name", "email")),
                        Arguments.of(List.of("originalPassword", "newPassword", "", "email")),
                        Arguments.of(List.of("originalPassword", "newPassword", "name", "")),
                        Arguments.of(Arrays.asList(null, "newPassword", "name", "email")),
                        Arguments.of(Arrays.asList("originalPassword", null, "name", "email")),
                        Arguments.of(Arrays.asList("originalPassword", "newPassword", null, "email")),
                        Arguments.of(Arrays.asList("originalPassword", "newPassword", "name", null))
                );
            }
        }

        @Nested
        class 수정하는_사용자의_기존_비밀번호가_다르다면 {

            @Test
            void 예외가_발생한다() {
                // given
                userRepository.save(new User(1L, "id", "password", "name", "email"));

                // expect
                assertThatThrownBy(
                        () -> userService.edit(1L, "differentPassword", "newPassword", "name", "email"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("입력한 기존 비밀번호와 실제 비밀번호가 다릅니다.");
            }
        }
    }
}
