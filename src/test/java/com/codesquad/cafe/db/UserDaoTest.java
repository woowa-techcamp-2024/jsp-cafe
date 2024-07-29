package com.codesquad.cafe.db;

import static com.codesquad.cafe.TestDataSource.createTable;
import static com.codesquad.cafe.TestDataSource.dataSource;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codesquad.cafe.db.entity.User;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserDaoTest {

    private static UserDao userDao;

    @BeforeAll
    static void beforeAll() {
        DataSource ds = dataSource();
        userDao = new UserDao(new JdbcTemplate(ds));
        createTable(ds);
    }

    @AfterEach
    void tearDown() {
        userDao.deleteAll();
    }

    User createUser(String username, String password, String name, String email) {
        return userDao.save(User.of(username, password, name, email));
    }

    @DisplayName("유저 저장")
    @Test
    void save() {
        //given
        User user = User.of("javajigi", "test1234", "박재성", "javajihi@gmail.com");
        //when
        User savedUser = userDao.save(user);

        //then
        assertEquals(user, savedUser);
        assertNotNull(user.getId());
        assertThat(savedUser)
                .hasFieldOrPropertyWithValue("username", user.getUsername())
                .hasFieldOrPropertyWithValue("password", user.getPassword())
                .hasFieldOrPropertyWithValue("name", user.getName())
                .hasFieldOrPropertyWithValue("email", user.getEmail())
                .hasFieldOrPropertyWithValue("createdAt", user.getCreatedAt())
                .hasFieldOrPropertyWithValue("updatedAt", user.getUpdatedAt())
                .hasFieldOrPropertyWithValue("deleted", user.isDeleted());
    }

    @DisplayName("유저 조회  - id")
    @Test
    void findById() {
        //given
        User user = createUser("javajigi", "test1234", "박재성", "javajigi@gamil.com");

        //when
        Optional<User> optionalUser = userDao.findById(user.getId());

        //then
        assertNotNull(optionalUser);
        assertThat(optionalUser.get())
                .hasFieldOrPropertyWithValue("username", user.getUsername())
                .hasFieldOrPropertyWithValue("password", user.getPassword())
                .hasFieldOrPropertyWithValue("name", user.getName())
                .hasFieldOrPropertyWithValue("email", user.getEmail())
                .hasFieldOrPropertyWithValue("createdAt", user.getCreatedAt())
                .hasFieldOrPropertyWithValue("updatedAt", user.getUpdatedAt())
                .hasFieldOrPropertyWithValue("deleted", user.isDeleted());

    }

    @DisplayName("유저 조회  - username")
    @Test
    void findByUsername() {
        //given
        User user = createUser("javajigi", "test1234", "박재성", "javajigi@gamil.com");

        //when
        Optional<User> optionalUser = userDao.findByUsername(user.getUsername());

        //then
        assertNotNull(optionalUser);
        assertThat(optionalUser.get())
                .hasFieldOrPropertyWithValue("username", user.getUsername())
                .hasFieldOrPropertyWithValue("password", user.getPassword())
                .hasFieldOrPropertyWithValue("name", user.getName())
                .hasFieldOrPropertyWithValue("email", user.getEmail())
                .hasFieldOrPropertyWithValue("createdAt", user.getCreatedAt())
                .hasFieldOrPropertyWithValue("updatedAt", user.getUpdatedAt())
                .hasFieldOrPropertyWithValue("deleted", user.isDeleted());
    }

    @DisplayName("유저 전체 조회")
    @Test
    void findAll() {
        //given
        userDao.deleteAll();
        User firstUser = createUser("user1", "test1234", "user", "javajigi@gamil.com");
        User secondUser = createUser("user2", "test1234", "user", "javajigi@gamil.com");
        User thirdUser = createUser("user3", "test1234", "user", "javajigi@gamil.com");

        //when
        List<User> users = userDao.findAll();

        //then
        assertEquals(3, users.size());
        assertTrue(users.containsAll(List.of(firstUser, secondUser, thirdUser)));
    }

    @DisplayName("유저 전체 삭제")
    @Test
    void deleteAll() {
        //given
        User firstUser = createUser("user1", "test1234", "user", "javajigi@gamil.com");
        User secondUser = createUser("user2", "test1234", "user", "javajigi@gamil.com");
        User thirdUser = createUser("user3", "test1234", "user", "javajigi@gamil.com");

        //when
        userDao.deleteAll();

        //then
        assertEquals(0, userDao.findAll().size());
    }
}
