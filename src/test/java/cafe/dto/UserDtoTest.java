package cafe.dto;

import cafe.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {

    @Test
    void 올바른_인자가_들어간_객체를_생성한다() {
        // given
        String id = "id";
        User user = User.of("id", "name", "password", "email@email");

        // when
        UserDto userDto = new UserDto(id, user);

        // then
        assertEquals(userDto.getId(), id);
        assertEquals(userDto.getUser(), user);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUserDtoInputs")
    void 인자에_null이_들어가면_오류가_발생한다(String id, User user) {
        // given, when, then
        assertThrows(IllegalArgumentException.class, () -> new UserDto(id, user));
    }

    private static Object[] provideInvalidUserDtoInputs() {
        return new Object[] {
            new Object[] {null, User.of("id", "name", "password", "email@email")},
            new Object[] {"id", null}
        };
    }
}