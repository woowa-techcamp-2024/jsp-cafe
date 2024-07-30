package cafe.domain.entity;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {
    @Test
    void 올바른_인자가_들어간_객체를_생성한다() {
        // given
        String userid = "userid";
        String name = "name";
        String password = "password";
        String email = "email@email";

        // when
        User user = User.of(userid, name, password, email);

        // then
        assertEquals(user.getUserId(), userid);
        assertEquals(user.getName(), name);
        assertEquals(user.getPassword(), password);
        assertEquals(user.getEmail(), email);
    }

    @Test
    void 이메일이_유효하지_않으면_오류가_발생한다() {
        // given
        String userid = "test";
        String name = "test";
        String password = "test";
        String email = "test";

        // when, then
        assertThrows(IllegalArgumentException.class, () -> User.of(userid, name, password, email));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUserInputs")
    void 인자에_null이_들어가면_오류가_발생한다(String userid, String name, String password, String email) {
        // given, when, then
        assertThrows(IllegalArgumentException.class, () -> User.of(userid, name, password, email));
    }

    private static Stream<Arguments> provideInvalidUserInputs() {
        return Stream.of(
                Arguments.of(null, "test", "test", "test@test"),
                Arguments.of("test", null, "test", "test@test"),
                Arguments.of("test", "test", null, "test@test"),
                Arguments.of("test", "test", "test", null)
        );
    }
}