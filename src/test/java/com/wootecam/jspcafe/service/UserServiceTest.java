package com.wootecam.jspcafe.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wootecam.jspcafe.repository.UserRepository;
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
    }
}
