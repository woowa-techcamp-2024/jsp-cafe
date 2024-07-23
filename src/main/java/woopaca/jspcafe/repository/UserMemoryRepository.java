package woopaca.jspcafe.repository;

import woopaca.jspcafe.model.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserMemoryRepository implements UserRepository {

    private static final Map<String, User> users = new ConcurrentHashMap<>();

    /**
     * 사용자 저장 - 사용자의 UUID id를 생성 후 저장
     * @param user 사용자
     */
    @Override
    public void save(User user) {
        String uniqueId = user.generateUniqueId();
        if (users.containsKey(uniqueId)) {
            throw new IllegalArgumentException("[ERROR] duplicate key!: " + uniqueId);
        }
        users.put(uniqueId, user);
    }

    /**
     * 모든 사용자 조회
     * @return 사용자 목록
     */
    @Override
    public List<User> findAll() {
        return users.values()
                .stream()
                .toList();
    }
}
