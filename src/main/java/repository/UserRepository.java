package repository;

import domain.Users;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void saveUser(Users user);

    Optional<Users> findById(Long id);

    Optional<Users> findByUserId(String password);

    List<Users> findAll();

}
