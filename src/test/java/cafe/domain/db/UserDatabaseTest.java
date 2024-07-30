package cafe.domain.db;

import cafe.domain.entity.User;
import cafe.domain.util.DatabaseConnector;
import cafe.domain.util.H2Connector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDatabaseTest {
    private static DatabaseConnector connector;
    private static UserDatabase userDatabase;

    @BeforeAll
    static void setUp() {
        connector = new H2Connector();
        userDatabase = new UserDatabase(connector);
    }

    @AfterEach
    void tearDown() {
        userDatabase.deleteAll();
    }

    @Test
    void 올바른_유저를_추가한다() {
        // given
        String id = "id";
        String name = "name";
        String password = "password";
        String email = "email@email";

        // when
        userDatabase.insert(User.of(id, name, password, email));
        User user = userDatabase.selectById(id);

        // then
        assertEquals(user.getUserId(), id);
        assertEquals(user.getName(), name);
        assertEquals(user.getPassword(), password);
        assertEquals(user.getEmail(), email);
    }

    @Test
    void 조회하는_유저가_없으면_null을_반환한다() {
        // given
        String id = "id";

        // when
        User user = userDatabase.selectById(id);

        // then
        assertNull(user);
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

        // when
        userDatabase.insert(User.of(id1, name1, password1, email1));
        userDatabase.insert(User.of(id2, name2, password2, email2));

        // then
        assertEquals(userDatabase.selectAll().size(), 2);
        assertTrue(userDatabase.selectAll().containsKey(id1));
        assertTrue(userDatabase.selectAll().containsKey(id2));
    }

    @Test
    void 아이디에_맞는_유저를_수정한다() {
        // given
        String id = "id";
        String name = "name";
        String password = "password";
        String email = "email@email";

        // when
        userDatabase.insert(User.of(id, name, password, email));
        userDatabase.update(id, User.of(id, name, "newPassword", "newEmail@newEmail"));

        // then
        User user = userDatabase.selectById(id);
        assertEquals(user.getPassword(), "newPassword");
        assertEquals(user.getEmail(), "newEmail@newEmail");
    }
}