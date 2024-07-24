package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.models.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemUserRepository implements UserRepository {

    private static AtomicLong sequence_id = new AtomicLong(1L);
    // id, User 로 생성한 맵을 데이터 베이스처럼 사용한다.
    private static final Map<Long, User> users = new ConcurrentHashMap<>();

    @Override
    public Long save(String userId, String password, String name, String email) {
        Long id = sequence_id.getAndIncrement();
        users.put(id, new User(id, userId, password, name, email));
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
    public Long update(Long id, String userId, String updatedName, String updatedEmail) {
        User user = users.get(id);
        user.update(userId, updatedName, updatedEmail);
        return id;
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
}
