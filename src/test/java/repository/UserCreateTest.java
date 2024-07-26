package repository;

import domain.Users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class UserCreateTest {

    private Map<Long, Users> store = new HashMap<>();
    private UserRepository userRepository = new MemoryUserRepository(store);

    @BeforeEach
    void clearStore(){
        store.clear();
    }

    @Test
    @DisplayName("유저가 저장되어야 한다.")
    public void testSaveUser() {

        // given
        Users user = new Users("test", "test", "test", "test@test");

        // when
        userRepository.saveUser(user);

        // then
        assertThat(userRepository.findById(user.getId())).isNotEmpty();
    }

    @Test
    @DisplayName("유저가 저장되지 않아야 한다.")
    public void testSaveUserWithEmpty() {

        // given
        Users user = new Users("", "", "", "");

        // when
        userRepository.saveUser(user);

        // then
        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    @DisplayName("여러 유저를 저장해야한다.")
    public void testSaveMultipleUsers() {

            // given
            Users user1 = new Users("test1", "test1", "test1", "test1@test");
            Users user2 = new Users("test2", "test2", "test2", "test2@test");
            Users user3 = new Users("test3", "test3", "test3", "test3@test");

            // when
            userRepository.saveUser(user1);
            userRepository.saveUser(user2);
            userRepository.saveUser(user3);

            // then
            assertThat(userRepository.findAll()).hasSize(3);
    }
}