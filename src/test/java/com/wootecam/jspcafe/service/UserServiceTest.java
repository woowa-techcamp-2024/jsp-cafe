package com.wootecam.jspcafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wootecam.jspcafe.model.User;
import com.wootecam.jspcafe.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
        class 만약_회원가입_정보가_비어있다면 {

            @Test
            void 예외를_발생시킨다() {
                assertThatThrownBy(() -> userService.signup("", "password", "name", "email"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("회원가입 시 모든 정보를 입력해야 합니다.");
            }
        }

        @Nested
        class 만약_회원가입_정보에_null이_포함되면 {

            @Test
            void 예외를_발생시킨다() {
                assertThatThrownBy(() -> userService.signup(null, "password", "name", "email"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("회원가입 시 모든 정보를 입력해야 합니다.");
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
}
