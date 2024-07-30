package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.models.User;

import java.util.List;

public interface UserRepository {
    Long save(String userId, String password, String name, String email);

    User findById(Long userId);

    List<User> findAll();

    Long update(Long id, String updatedName, String updatedEmail);

    void deleteAll();

    boolean isExistedByUserId(String userId);

    User findByUserId(String w);
}
