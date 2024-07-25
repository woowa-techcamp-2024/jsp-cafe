package org.example.jspcafe.user.repository;

import org.example.jspcafe.AbstractRepositoryTestSupport;
import org.example.jspcafe.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

class JdbcUserRepositoryTest extends AbstractRepositoryTestSupport {

    private UserRepository userRepository = new JdbcUserRepository(super.connectionManager);

    @Override
    protected void deleteAllInBatch() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("findAllById에서 빈 목록을 조회하면 빈 목록을 반환한다")
    @Test
    void findAllByIdEmpty() {
        // given
        List<Long> userIds = List.of();

        // when
        List<User> foundUsers = userRepository.findAllById(userIds);

        // then
        assertThat(foundUsers).isEmpty();
    }

    @DisplayName("deleteAllInBatch 메서드를 호출하면 저장된 모든 유저를 삭제한다")
    @Test
    void deleteAllInBatchTest() {
        // given
        List<User> users = List.of(
                new User("nickname1", "email1@example.com", "password1", LocalDateTime.of(2021, 1, 1, 0, 0)),
                new User("nickname2", "email2@example.com", "password2", LocalDateTime.of(2021, 1, 1, 0, 0)),
                new User("nickname3", "email3@example.com", "password3", LocalDateTime.of(2021, 1, 1, 0, 0)),
                new User("nickname4", "email4@example.com", "password4", LocalDateTime.of(2021, 1, 1, 0, 0))
        );

        users.forEach(userRepository::save);

        // when
        userRepository.deleteAllInBatch();

        // then
        assertThat(userRepository.findAll()).isEmpty();
    }

    @DisplayName("nickname으로 유저를 조회할 수 있다")
    @Test
    void findByNickname() {
        // given
        String nickname = "nickname";
        User user = new User(nickname, "email@example.com", "password", LocalDateTime.of(2021, 1, 1, 0, 0));
        userRepository.save(user);

        // when
        Optional<User> maybeUser = userRepository.findByNickname(nickname);

        // then
        assertThat(maybeUser).isPresent()
                .get()
                .extracting("nickname.value")
                .isEqualTo(nickname);
    }

    @DisplayName("없는 nickname으로 유저를 조회하면 빈 Optional을 반환한다")
    @Test
    void findByNicknameNotFound() {
        // given
        String nickname = "nickname";

        // when
        Optional<User> maybeUser = userRepository.findByNickname(nickname);

        // then
        assertThat(maybeUser).isNotPresent();
    }


    @DisplayName("email로 유저를 조회할 수 있다")
    @Test
    void findByEmail() {
        // given
        String email = "email@example.com";
        User user = new User("nickname", email, "password", LocalDateTime.of(2021, 1, 1, 0, 0));
        userRepository.save(user);

        // when
        Optional<User> maybeUser = userRepository.findByEmail(email);

        // then
        assertThat(maybeUser).isPresent()
                .get()
                .extracting("email.value")
                .isEqualTo(email);
    }

    @DisplayName("없는 email로 유저를 조회하면 빈 Optional을 반환한다")
    @Test
    void findByEmailNotFound() {
        // given
        String email = "email@example.com";

        // when
        Optional<User> maybeUser = userRepository.findByEmail(email);

        // then
        assertThat(maybeUser).isNotPresent();
    }

    @DisplayName("저장된 모든 유저를 조회할 수 있다")
    @Test
    void findAll() {
        // given
        List<User> users = List.of(
                new User("nickname1", "email1@example.com", "password1", LocalDateTime.of(2021, 1, 1, 0, 0)),
                new User("nickname2", "email2@example.com", "password2", LocalDateTime.of(2021, 1, 1, 0, 0)),
                new User("nickname3", "email3@example.com", "password3", LocalDateTime.of(2021, 1, 1, 0, 0))
        );

        users.forEach(userRepository::save);

        // when
        List<User> foundUsers = userRepository.findAll();

        // then
        assertThat(foundUsers)
                .hasSize(3)
                .extracting("nickname.value", "email.value", "password.value")
                .containsExactly(
                        tuple("nickname1", "email1@example.com", "password1"),
                        tuple("nickname2", "email2@example.com", "password2"),
                        tuple("nickname3", "email3@example.com", "password3")
                );
    }

    @DisplayName("userId 목록으로 유저를 조회할 수 있다")
    @Test
    void findAllById() {
        // given
        List<User> users = List.of(
                new User("nickname1", "email1@example.com", "password1", LocalDateTime.of(2021, 1, 1, 0, 0)),
                new User("nickname2", "email2@example.com", "password2", LocalDateTime.of(2021, 1, 1, 0, 0)),
                new User("nickname3", "email3@example.com", "password3", LocalDateTime.of(2021, 1, 1, 0, 0)),
                new User("nickname4", "email4@example.com", "password4", LocalDateTime.of(2021, 1, 1, 0, 0))
        );


        List<Long> userIds = users.stream()
                .map(user -> userRepository.save(user).getUserId())
                .toList();

        // when
        List<User> foundUsers = userRepository.findAllById(userIds);


        // then
        assertThat(foundUsers)
                .hasSize(4)
                .extracting(User::getUserId)
                .containsAll(userIds);
    }
    @DisplayName("기존 유저를 저장하면 기존 유저를 반환한다")
    @Test
    void saveAlreadyExists() {
        // given
        String nickname = "nickname";
        String email = "email@example.com";
        String password = "password";
        User user = new User(nickname, email, password, LocalDateTime.of(2021, 1, 1, 0, 0));
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
        User user = new User(nickname, email, password, LocalDateTime.of(2021, 1, 1, 0, 0));

        // when
        User savedUser = userRepository.save(user);

        // then
        Long savedUserId = savedUser.getUserId();
        Optional<User> foundUser = userRepository.findById(savedUserId);

        assertThat(foundUser).isPresent()
                .get()
                .extracting("userId", "nickname.value", "email.value", "password.value")
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
        User user = new User(nickname, email, password, LocalDateTime.of(2021, 1, 1, 0, 0));

        User savedUser = userRepository.save(user);
        savedUser.updatePassword("newPassword");

        // when
        userRepository.update(savedUser);


        // then
        Optional<User> updatedUser = userRepository.findById(savedUser.getUserId());
        assertThat(updatedUser).isPresent()
                .get()
                .extracting("nickname.value", "email.value", "password.value")
                .contains(nickname, email, "newPassword");
    }

    @DisplayName("유저를 삭제할 수 있다")
    @Test
    void delete() {
        // given
        String nickname = "nickname";
        String email = "email@example.com";
        String password = "password";
        User user = new User(nickname, email, password, LocalDateTime.of(2021, 1, 1, 0, 0));
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
        User user = new User("nickname", "email@example.com", "password", LocalDateTime.of(2021, 1, 1, 0, 0));

        // when & then
        assertThatThrownBy(() -> userRepository.delete(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id는 null일 수 없습니다.");
    }

    @DisplayName("ID가 null인 엔티티를 업데이트할 때 예외를 던진다")
    @Test
    void updateEntityWithNullId() {
        // given
        User user = new User("nickname", "email@example.com", "password", LocalDateTime.of(2021, 1, 1, 0, 0));

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
        User user = new User(nickname, email, password, LocalDateTime.of(2021, 1, 1, 0, 0));

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser)
                .isNotNull()
                .extracting(User::getUserId)
                .isNotNull();
    }

}