package woowa.camp.jspcafe.repository;

import java.util.List;
import java.util.Optional;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.repository.dto.UserUpdateRequest;

public interface UserRepository {

    Long save(User user);

    List<User> findAll();

    Optional<User> findById(Long id);

    void update(Long userId, UserUpdateRequest userUpdateRequest);
}
