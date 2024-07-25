package repository;

import domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void saveUser(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUserId(String password);

    List<User> findAll();

}
