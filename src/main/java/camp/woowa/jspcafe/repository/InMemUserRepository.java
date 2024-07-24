package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.models.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemUserRepository implements UserRepository {

    // id, User 로 생성한 맵을 데이터 베이스처럼 사용한다.
    private static final Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public String save(String userId, String password, String name, String email) {
        users.put(userId, new User(userId, password, name, email));
        return userId;
    }

    @Override
    public User findById(String userId) {
        return users.get(userId);
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }


}
