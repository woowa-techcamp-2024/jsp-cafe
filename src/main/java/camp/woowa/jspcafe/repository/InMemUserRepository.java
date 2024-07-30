package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.model.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemUserRepository implements UserRepository {

    private static AtomicLong sequence_id = new AtomicLong(1L);
    // id, User 로 생성한 맵을 데이터 베이스처럼 사용한다.
    private static final Map<Long, User> users = new ConcurrentHashMap<>();

    @Override
    public Long save(User user) {
        Long id = sequence_id.getAndIncrement();
        users.put(id, new User(id, user.getUserId(), user.getPassword(), user.getName(), user.getEmail()));
        return id;
    }

    @Override
    public User findById(Long userId) {
        return users.get(userId);
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    @Override
    public Long update(User user) {
        users.put(user.getId(), user);

        return user.getId();
    }

    @Override
    public void deleteAll() {
        users.clear();
        sequence_id.set(1L);
    }

    @Override
    public boolean isExistedByUserId(String userId) {
        // TODO: 매우 느림
        for (User user : users.values()) {
            if (user.getUserId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public User findByUserId(String w) {
        return users.values().stream()
                .filter(user -> user.getUserId().equals(w))
                .findFirst()
                .orElse(null);
    }
}
