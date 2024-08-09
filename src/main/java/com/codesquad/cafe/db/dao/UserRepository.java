package com.codesquad.cafe.db.dao;

import java.util.List;
import java.util.Optional;

import com.codesquad.cafe.db.domain.User;

public interface UserRepository {

	User save(User user);

	Optional<User> findById(Long id);

	Optional<User> findByUsername(String username);

	List<User> findAll();

	void deleteAll();

}
