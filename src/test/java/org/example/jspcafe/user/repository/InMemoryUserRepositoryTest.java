package org.example.jspcafe.user.repository;

import org.example.jspcafe.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InMemoryUserRepositoryTest {

    private InMemoryUserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
    }

    @DisplayName("기존 유저를 저장하면 기존 유저를 반환한다")
    @Test
    void saveAlreadyExists() {
        // given
        String nickname = "nickname";
        String email = "email@example.com";
        String password = "password";
        User user = new User(nickname, email, password);
        User savedUser = userRepository.save(user);

        // when
        User savedUser2 = userRepository.save(savedUser);

        // then
        assertThat(savedUser2).isNotNull()
                .extracting(u -> u.getNickname().getValue(), u -> u.getEmail().getValue(), u -> u.getPassword().getValue())
                .contains(nickname, email, password);
    }

    @DisplayName("유저를 저장하고 조회할 수 있다")
    @Test
    void saveAndFindById() {
        // given
        String nickname = "nickname";
        String email = "email@example.com";
        String password = "password";
        User user = new User(nickname, email, password);

        // when
        User savedUser = userRepository.save(user);

        // then
        Long savedUserId = savedUser.getUserId();
        Optional<User> foundUser = userRepository.findById(savedUserId);

        assertThat(foundUser).isPresent()
                .get()
                .extracting(User::getUserId, u -> u.getNickname().getValue(), u -> u.getEmail().getValue(), u -> u.getPassword().getValue())
                .contains(savedUserId, nickname, email, password);
    }

    @DisplayName("저장되지 않은 유저를 조회할 때 null을 반환한다")
    @Test
    void findByIdNotFound() {
        // when
        Optional<User> foundUser = userRepository.findById(1L);

        // then
        assertThat(foundUser).isNotPresent();
    }

    @DisplayName("유저를 업데이트할 수 있다")
    @Test
    void update() {
        // given
        String nickname = "nickname";
        String email = "email@example.com";
        String password = "password";
        User user = new User(nickname, email, password);

        User savedUser = userRepository.save(user);
        savedUser.updatePassword("newPassword");

        // when
        userRepository.update(savedUser);


        // then
        Optional<User> updatedUser = userRepository.findById(savedUser.getUserId());
        assertThat(updatedUser).isPresent()
                .get()
                .extracting(u -> u.getNickname().getValue(), u -> u.getEmail().getValue(), u -> u.getPassword().getValue())
                .contains(nickname, email, "newPassword");
    }

    @DisplayName("유저를 삭제할 수 있다")
    @Test
    void delete() {
        // given
        String nickname = "nickname";
        String email = "email@example.com";
        String password = "password";
        User user = new User(nickname, email, password);
        User savedUser = userRepository.save(user);

        // when
        userRepository.delete(savedUser);
        Optional<User> deletedUser = userRepository.findById(savedUser.getUserId());

        // then
        assertThat(deletedUser).isNotPresent();
    }

    @DisplayName("null 엔티티를 저장할 때 예외를 던진다")
    @Test
    void saveNullEntity() {

        // when & then
        assertThatThrownBy(() -> userRepository.save(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Entity는 null일 수 없습니다.");
    }

    @DisplayName("ID가 null인 엔티티를 삭제할 때 예외를 던진다")
    @Test
    void deleteEntityWithNullId() {
        // given
        User user = new User("nickname", "email@example.com", "password");

        // when & then
        assertThatThrownBy(() -> userRepository.delete(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id는 null일 수 없습니다.");
    }

    @DisplayName("ID가 null인 엔티티를 업데이트할 때 예외를 던진다")
    @Test
    void updateEntityWithNullId() {
        // given
        User user = new User("nickname", "email@example.com", "password");

        // when // then
        assertThatThrownBy(() -> userRepository.update(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id는 null일 수 없습니다.");
    }

    @DisplayName("유저 저장 시 PK가 할당된다")
    @Test
    void saveAssignsPK() {
        // given
        String nickname = "nickname";
        String email = "email@example.com";
        String password = "password";
        User user = new User(nickname, email, password);

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser)
                .isNotNull()
                .extracting(User::getUserId)
                .isNotNull();
    }
}
