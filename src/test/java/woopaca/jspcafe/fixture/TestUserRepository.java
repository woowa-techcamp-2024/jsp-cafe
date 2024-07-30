package woopaca.jspcafe.fixture;

import woopaca.jspcafe.model.User;
import woopaca.jspcafe.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TestUserRepository implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public void save(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findAny();
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        return users.values().stream()
                .filter(user -> user.getNickname().equals(nickname))
                .findAny();
    }
}
