package cafe.domain.db;

import cafe.domain.entity.User;
import cafe.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionDatabaseTest {
    private final SessionDatabase sessionDatabase = new SessionDatabase();

    @BeforeEach
    void setUp() {
        sessionDatabase.deleteAll();
    }

    @Test
    void 올바른_세션을_추가한다() {
        // given
        String sessionId = "sessionId";
        User user = User.of("userId", "name", "password", "email@email");
        UserDto userDto = new UserDto(sessionId, user);

        // when
        sessionDatabase.insert(userDto);

        // then
        assertEquals(user, sessionDatabase.selectById(sessionId));
    }

    @Test
    void 조회하는_유저가_없으면_null을_반환한다() {
        // given, when
        User user = (User) sessionDatabase.selectById("sessionId");

        // then
        assertNull(user);
    }

    @Test
    void 모든_세션을_조회한다() {
        // given
        String sessionId1 = "sessionId1";
        User user1 = User.of("userId1", "name1", "password1", "email1@email1");
        UserDto userDto1 = new UserDto(sessionId1, user1);

        String sessionId2 = "sessionId2";
        User user2 = User.of("userId2", "name2", "password2", "email2@email2");
        UserDto userDto2 = new UserDto(sessionId2, user2);

        // when
        sessionDatabase.insert(userDto1);
        sessionDatabase.insert(userDto2);

        // then
        assertEquals(2, sessionDatabase.selectAll().size());
        assertTrue(sessionDatabase.selectAll().containsKey(sessionId1));
        assertTrue(sessionDatabase.selectAll().containsKey(sessionId2));
    }

    @Test
    void 아이디에_해당하는_세션을_삭제한다() {
        // given
        String sessionId = "sessionId";
        User user = User.of("userId", "name", "password", "email@email");
        UserDto userDto = new UserDto(sessionId, user);

        // when
        sessionDatabase.insert(userDto);
        sessionDatabase.deleteById(sessionId);

        // then
        assertNull(sessionDatabase.selectById(sessionId));
    }

    @Test
    void 모든_세션을_삭제한다() {
        // given
        String sessionId1 = "sessionId1";
        User user1 = User.of("userId1", "name1", "password1", "email1@email1");
        UserDto userDto1 = new UserDto(sessionId1, user1);

        String sessionId2 = "sessionId2";
        User user2 = User.of("userId2", "name2", "password2", "email2@email2");
        UserDto userDto2 = new UserDto(sessionId2, user2);

        // when
        sessionDatabase.insert(userDto1);
        sessionDatabase.insert(userDto2);
        sessionDatabase.deleteAll();

        // then
        assertEquals(0, sessionDatabase.selectAll().size());
    }

    @Test
    void 삭제하는_유저가_없으면_예외가_발생한다() {
        // given, when, then
        assertThrows(IllegalArgumentException.class, () -> sessionDatabase.deleteById(1));
    }
}