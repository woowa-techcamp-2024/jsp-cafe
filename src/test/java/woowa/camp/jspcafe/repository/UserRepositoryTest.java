package woowa.camp.jspcafe.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.fixture.UserFixture;
import woowa.camp.jspcafe.infra.DatabaseConnector;
import woowa.camp.jspcafe.repository.dto.UserUpdateRequest;
import woowa.camp.jspcafe.repository.user.DBUserRepository;
import woowa.camp.jspcafe.repository.user.UserRepository;
import woowa.camp.jspcafe.utils.FixedDateTimeProvider;
import woowa.camp.jspcafe.infra.time.DateTimeProvider;

@ExtendWith(UserDBSetupExtension.class)
class UserRepositoryTest {

    DatabaseConnector connector = new DatabaseConnector();
    DateTimeProvider fixedDateTime = new FixedDateTimeProvider(2024, 7, 25);
    UserRepository userRepository = new DBUserRepository(connector);

    @Test
    @DisplayName("[Success]회원가입을 성공하면, 사용자 ID로 조회할 수 있다")
    void test() {
        User user = UserFixture.createUser(1, fixedDateTime.getNow());
        Long saveUserId = userRepository.save(user);
        Optional<User> findUserOpt = userRepository.findById(saveUserId);
        assertThat(findUserOpt.isPresent()).isTrue();

        User findUser = findUserOpt.get();
        assertThat(findUser).isEqualTo(user);
        assertThat(findUser.getId()).isNotNull();
        assertThat(findUser.getId()).isSameAs(saveUserId);

        assertThat(findUser.getId()).isEqualTo(user.getId());
        assertThat(findUser.getNickname()).isEqualTo(user.getNickname());
        assertThat(findUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(findUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("[Success] 회원가입을 성공할 때마다 id가 1씩 증가한다.")
    void test2() {
        User user1 = UserFixture.createUser(1, fixedDateTime.getNow());
        User user2 = UserFixture.createUser(2, fixedDateTime.getNow());

        Long saveUserId1 = userRepository.save(user1);
        Long saveUserId2 = userRepository.save(user2);

        assertThat(saveUserId1).isNotNull();
        assertThat(saveUserId2).isNotNull();
        assertThat(saveUserId1 + 1).isEqualTo(saveUserId2);
    }

    @Test
    @DisplayName("회원목록 전체를 조회한다")
    void test3() {
        User user1 = UserFixture.createUser(1, fixedDateTime.getNow());
        User user2 = UserFixture.createUser(2, fixedDateTime.getNow());

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();
        assertThat(users).isEqualTo(List.of(user1, user2));
    }

    @Nested
    @DisplayName("Describe_회원정보를 갱신하는 기능은")
    class UpdateTest {

        private UserRepository userRepository = new DBUserRepository(connector);
        private User testUser;

        @BeforeEach
        void setUp() {
            testUser = new User("test@email.com", "oldNickname", "oldPassword", fixedDateTime.getNow());
            Long userId = userRepository.save(testUser);
            testUser.setId(userId);
        }

        @Test
        @DisplayName("[Success] 비밀번호를 갱신할 수 있다")
        void updatePassword() {
            // given
            String newPassword = "newPassword";
            UserUpdateRequest updateDto = new UserUpdateRequest(newPassword, testUser.getNickname());

            // when
            User updatedUser = userRepository.update(testUser, updateDto);

            // then
            assertThat(updatedUser.getPassword()).isEqualTo(newPassword);
            assertThat(updatedUser.getNickname()).isEqualTo(testUser.getNickname());
        }

        @Test
        @DisplayName("[Success] 닉네임을 갱신할 수 있다")
        void updateNickname() {
            // given
            String newNickname = "newNickname";
            UserUpdateRequest updateRequest = new UserUpdateRequest(testUser.getPassword(), newNickname);

            // when
            User updatedUser = userRepository.update(testUser, updateRequest);

            // then
            assertThat(updatedUser.getNickname()).isEqualTo(newNickname);
            assertThat(updatedUser.getPassword()).isEqualTo(testUser.getPassword());
        }

        @Test
        @DisplayName("[Success] 비밀번호와 닉네임을 동시에 갱신할 수 있다")
        void updatePasswordAndNickname() {
            // given
            String newPassword = "newPassword";
            String newNickname = "newNickname";
            UserUpdateRequest updateDto = new UserUpdateRequest(newPassword, newNickname);

            // when
            User updatedUser = userRepository.update(testUser, updateDto);

            // then
            assertThat(updatedUser.getPassword()).isEqualTo(newPassword);
            assertThat(updatedUser.getNickname()).isEqualTo(newNickname);
        }

        @Test
        @DisplayName("[Success] 존재하지 않는 사용자 ID면 예외가 발생한다")
        void notExistUser() {
            String newPassword = "newPassword";
            String newNickname = "newNickname";
            UserUpdateRequest updateDto = new UserUpdateRequest(newPassword, newNickname);
            User user = UserFixture.createUser1();
            user.setId(12345L);

            assertThatThrownBy(() -> userRepository.update(user, updateDto))
                    .isInstanceOf(RuntimeException.class);
        }

    }
}