package lass9436.user.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

	private final List<User> users = new ArrayList<>();

	@Override
	public User save(User user) {
		users.stream()
			.filter(u -> u.getUserId().equals(user.getUserId()))
			.findFirst()
			.ifPresentOrElse(
				existingUser -> users.set(users.indexOf(existingUser), user),
				() -> users.add(user)
			);
		return user;
	}

	@Override
	public User findByUserId(String userId) {
		Optional<User> user = users.stream()
			.filter(u -> u.getUserId().equals(userId))
			.findFirst();
		return user.orElse(null);
	}

	@Override
	public List<User> findAll() {
		return new ArrayList<>(users);
	}

	@Override
	public void deleteByUserId(String userId) {
		users.removeIf(user -> user.getUserId().equals(userId));
	}
}

