package codesquad.jspcafe.domain.user.repository;

import codesquad.jspcafe.domain.user.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    User update(User user);

    Optional<User> findByUserId(String userId);

    List<User> findAll();
}
