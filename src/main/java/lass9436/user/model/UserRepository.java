package lass9436.user.model;

import java.util.List;

public interface UserRepository {
	User save(User user); // Create or Update

	User findByUserId(String userId); // Read by ID

	List<User> findAll(); // Read all

	void deleteByUserId(String userId); // Delete by ID

	User findByUserSeq(long userId);
}
