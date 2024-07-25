package woowa.camp.jspcafe.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.repository.dto.UserUpdateRequest;

public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong();

    @Override
    public Long save(User user) {
        long currentId = this.id.incrementAndGet();
        user.setId(currentId);
        users.putIfAbsent(currentId, user);
        return currentId;
    }

    @Override
    public List<User> findAll() {
        return users.values()
                .stream()
                .toList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User update(User user, UserUpdateRequest updateRequest) {
        String nickname = updateRequest.nickname();
        String password = updateRequest.password();

        User updateUser = new User(user.getEmail(), nickname, password, user.getRegisterAt());
        updateUser.setId(user.getId());
        users.remove(user.getId());
        users.put(updateUser.getId(), updateUser);
        return updateUser;
    }
}
