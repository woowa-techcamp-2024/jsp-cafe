package woopaca.jspcafe.fixture;

import woopaca.jspcafe.model.User;
import woopaca.jspcafe.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class TestRepositoryFixture {

    public static UserRepository testUserRepository() {
        return new TestUserRepository();
    }

    public static class TestUserRepository implements UserRepository {

        @Override
        public void save(User user) {
        }

        @Override
        public List<User> findAll() {
            return List.of(
                    UserFixture.testUser(),
                    UserFixture.testUser(),
                    UserFixture.testUser()
            );
        }

        @Override
        public Optional<User> findById(String id) {
            if (id.equals(UserFixture.testUser().getId())) {
                return Optional.of(UserFixture.testUser());
            }
            return Optional.empty();
        }

        @Override
        public Optional<User> findByUsername(String username) {
            if (username.equals(UserFixture.testUser().getUsername())) {
                return Optional.of(UserFixture.testUser());
            }
            return Optional.empty();
        }

        @Override
        public Optional<User> findByNickname(String nickname) {
            if (nickname.equals(UserFixture.testUser().getNickname())) {
                return Optional.of(UserFixture.testUser());
            }
            return Optional.empty();
        }
    }
}
