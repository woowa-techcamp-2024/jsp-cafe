package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.model.User;

import java.util.List;

public interface UserRepository {
    Long save(User user);

    User findById(Long userId);

    List<User> findAll();

    Long update(User user);

    void deleteAll();

    boolean isExistedByUserId(String userId);

    User findByUserId(String w);
}
