package org.example.cafe.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import org.example.cafe.application.dto.UserCreateDto;
import org.example.cafe.application.dto.UserUpdateDto;
import org.example.cafe.common.exception.BadAuthenticationException;
import org.example.cafe.common.exception.CafeException;
import org.example.cafe.common.exception.DataNotFoundException;
import org.example.cafe.domain.User;
import org.example.cafe.domain.UserRepository;
import org.example.cafe.infrastructure.UserJdbcRepository;
import org.example.cafe.infrastructure.database.DbConnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest {

    DbConnector dbConnector = new DbConnector().init();
    UserRepository userRepository = new UserJdbcRepository(dbConnector);
    UserService userService = new UserService(userRepository);

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createUser() {
        UserCreateDto userCreateDto = new UserCreateDto("userId", "password", "name", "email");

        userService.createUser(userCreateDto);

        assertEquals(1, userRepository.findAll().size());
    }

    @Test
    void create_user_throws_cafe_exception() {
        User existingUser = new User("user1", "password", "nickname", "email@example.com");
        userRepository.save(existingUser);
        UserCreateDto userCreateDto = new UserCreateDto("user1", "password", "nickname", "email@example.com");

        assertThrows(CafeException.class, () -> {
            userService.createUser(userCreateDto);
        });
    }

    @Test
    void updateUser() {
        UserCreateDto userCreateDto = new UserCreateDto("userId", "password", "name", "email");
        userService.createUser(userCreateDto);
        UserUpdateDto userUpdateDto = new UserUpdateDto("password", "password", "nickname", "email");

        userService.updateUser("userId", userUpdateDto);

        assertEquals("nickname", userRepository.findById("userId").getNickname());
    }

    @Test
    void update_user_throws_bad_authentication_exception() {
        User existingUser = new User("user1", "password", "nickname", "email@example.com");
        userRepository.save(existingUser);
        UserUpdateDto userUpdateDto = new UserUpdateDto("wrongPassword", "newPassword", "newNickname",
                "newEmail@example.com");

        assertThrows(BadAuthenticationException.class, () -> {
            userService.updateUser("user1", userUpdateDto);
        });
    }

    @Test
    void update_user_throws_data_not_found_exception() {
        UserUpdateDto userUpdateDto = new UserUpdateDto("password", "newPassword", "newNickname",
                "newEmail@example.com");

        assertThrows(DataNotFoundException.class, () -> {
            userService.updateUser("user1", userUpdateDto);
        });
    }

    @Test
    void findAll() {
        UserCreateDto userCreateDto = new UserCreateDto("userId", "password", "name", "email");
        userService.createUser(userCreateDto);

        int size = userService.findAll().size();

        assertEquals(1, size);
    }

    @Test
    void findById() {
        UserCreateDto userCreateDto = new UserCreateDto("userId", "password", "name", "email");
        userService.createUser(userCreateDto);

        User user = userService.findById("userId");

        assertEquals("userId", user.getUserId());
    }

    @Test
    void test_find_all_should_return_all_users() {
        List<User> expectedUsers = Arrays.asList(
                new User("user1", "pass1", "John", "john@example.com"),
                new User("user2", "pass2", "Jane", "jane@example.com")
        );
        for (User user : expectedUsers) {
            userRepository.save(user);
        }

        List<User> actualUsers = userService.findAll();

        assertEquals(expectedUsers.size(), actualUsers.size());
        for (int i = 0; i < expectedUsers.size(); i++) {
            assertEquals(expectedUsers.get(i), actualUsers.get(i));
        }
    }
}