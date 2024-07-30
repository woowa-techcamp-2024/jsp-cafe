package cafe.service;

import cafe.domain.db.SessionDatabase;
import cafe.domain.db.UserDatabase;
import cafe.domain.entity.User;
import cafe.domain.util.DatabaseConnector;
import cafe.domain.util.H2Connector;
import cafe.dto.UserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {
    private static UserDatabase userDatabase;
    private static SessionDatabase sessionDatabase;
    private static UserService userService;

    @BeforeAll
    static void setUp() {
        DatabaseConnector databaseConnector = new H2Connector();
        userDatabase = new UserDatabase(databaseConnector);
        sessionDatabase = new SessionDatabase();
        userService = new UserService(userDatabase, sessionDatabase);
    }

    @AfterEach
    void tearDown() {
        userDatabase.deleteAll();
        sessionDatabase.deleteAll();
    }

    @Test
    void 올바른_유저를_저장한다() {
        // given
        String id = "id";
        String name = "name";
        String password = "password";
        String email = "email@email";

        // when
        userService.save(id, name, password, email);

        // then
        assertEquals(1, userService.findAll().size());
    }

    @Test
    void 경로의_아이디로_유저를_조회한다() {
        // given
        String id = "id";
        String name = "name";
        String password = "password";
        String email = "email@email";
        userService.save(id, name, password, email);

        // when
        User user = userService.find("/users/id");

        // then
        assertEquals(id, user.getUserId());
        assertEquals(name, user.getName());
        assertEquals(password, user.getPassword());
        assertEquals(email, user.getEmail());
    }

    @Test
    void 조회_시_아이디에_해당하는_유저가_없다면_예외가_발생한다() {
        // given, when, then
        assertThrows(IllegalArgumentException.class, () -> userService.find("/users/id"));
    }

    @Test
    void 세션_아이디로_유저를_조회한다() {
        // given
        String id = "id";
        String name = "name";
        String password = "password";
        String email = "email@email";
        String sessionId = "sessionId";

        userService.save(id, name, password, email);
        sessionDatabase.insert(new UserDto(sessionId, User.of(id, name, password, email)));

        // when
        UserDto userDto = userService.findBySession("sessionId");

        // then
        assertEquals(id, userDto.getUser().getUserId());
        assertEquals(name, userDto.getUser().getName());
        assertEquals(password, userDto.getUser().getPassword());
        assertEquals(email, userDto.getUser().getEmail());
    }

    @Test
    void 조회_시_세션_아이디에_해당하는_유저가_없다면_예외가_발생한다() {
        // given, when, then
        assertThrows(IllegalArgumentException.class, () -> userService.findBySession("sessionId"));
    }

    @Test
    void 모든_유저를_조회한다() {
        // given
        String id1 = "id1";
        String name1 = "name1";
        String password1 = "password1";
        String email1 = "email1@email1";

        String id2 = "id2";
        String name2 = "name2";
        String password2 = "password2";
        String email2 = "email2@email2";

        userService.save(id1, name1, password1, email1);
        userService.save(id2, name2, password2, email2);

        // when
        int size = userService.findAll().size();

        // then
        assertEquals(2, size);
    }

    @Test
    void 경로의_아이디로_유저를_수정한다() {
        // given
        String id = "id";
        String name = "name";
        String password = "password";
        String email = "email@email";
        userService.save(id, name, password, email);

        // when
        userService.update("/users/id", name, "newPassword", "newEmail@newEmail", password);

        // then
        User user = userDatabase.selectById(id);
        assertEquals("newPassword", user.getPassword());
        assertEquals("newEmail@newEmail", user.getEmail());
    }

    @Test
    void 수정_시_아이디에_해당하는_유저가_없다면_예외가_발생한다() {
        // given, when, then
        assertThrows(IllegalArgumentException.class, () ->
                userService.update("/users/id", "name", "password", "email", "password"));
    }

    @Test
    void 수정_시_비밀번호가_틀리다면_예외가_발생한다() {
        // given
        String id = "id";
        String name = "name";
        String password = "password";
        String email = "email@email";
        userService.save(id, name, password, email);

        // when, then
        assertThrows(IllegalArgumentException.class, () ->
                userService.update("/users/id", name, "newPassword", "newEmail@newEmail", "wrongPassword"));
    }
}