package codesquad.user.domain;

import codesquad.common.exception.IncorrectPasswordException;
import codesquad.fixture.user.UserFixture;
import org.junit.jupiter.api.*;

class UserTest implements UserFixture {
    private User user;

    @BeforeEach
    void setUp() {
        user = alice();
    }

    @Nested
    @DisplayName("matches 메서드는")
    class MatchesIs {
        @Test
        @DisplayName("올바른 비밀번호면 true를 반환")
        void correctPassword() {
            Assertions.assertFalse(user.matches("alice_password"));
        }

        @Test
        @DisplayName("잘못된 비밀번호면 false를 반환")
        void wrongPassword() {
            Assertions.assertFalse(user.matches("wrong_password"));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class UpdateIs {
        @Test
        @DisplayName("올바른 비밀번호면 회원정보를 변경")
        void changeInfo() throws IncorrectPasswordException {
            user.update("password_alice", "newAlice", "newAlice@gmail.com");

            Assertions.assertEquals(user.getName(), "newAlice");
            Assertions.assertEquals(user.getEmail(), "newAlice@gmail.com");
        }

        @Test
        @DisplayName("잘못된 비밀번호면 IncorrectPasswordException 발생")
        void wrongPassword() {
            Assertions.assertThrows(IncorrectPasswordException.class,
                    () -> user.update("alice_password", "newAlice", "newAlice@gmail.com"));
        }
    }
}
