package woowa.camp.jspcafe.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.fixture.UserFixture;

class InMemoryUserRepositoryTest {

    UserRepository userRepository = new InMemoryUserRepository();

    @Test
    @DisplayName("회원가입을 성공하면, 사용자 ID로 조회할 수 있다")
    void test() {
        User user = UserFixture.createUser1();
        Long saveUserId = userRepository.save(user);
        Optional<User> findUserOpt = userRepository.findById(saveUserId);
        assertThat(findUserOpt.isPresent()).isTrue();

        User findUser = findUserOpt.get();
        assertThat(findUser).isEqualTo(user);
        assertThat(findUser.getId()).isNotNull();
        assertThat(findUser.getId()).isSameAs(saveUserId);
    }

    @Test
    @DisplayName("회원가입을 성공할 때마다 id가 1씩 증가한다.")
    void test2() {
        User user1 = UserFixture.createUser1();
        User user2 = UserFixture.createUser2();

        Long saveUserId1 = userRepository.save(user1);
        Long saveUserId2 = userRepository.save(user2);

        assertThat(saveUserId1).isNotNull();
        assertThat(saveUserId2).isNotNull();
        assertThat(saveUserId1 + 1).isEqualTo(saveUserId2);
    }

    @Test
    @DisplayName("회원목록 전체를 조회한다")
    void test3() {
        User user1 = UserFixture.createUser1();
        User user2 = UserFixture.createUser2();

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();
        assertThat(users).isEqualTo(List.of(user1, user2));
    }

}