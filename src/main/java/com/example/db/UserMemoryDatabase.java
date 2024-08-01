package com.example.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.example.entity.User;
import com.example.exception.BaseException;

public class UserMemoryDatabase implements UserDatabase {

	private final Map<String, User> users = new ConcurrentHashMap<>();

	@Override
	public String insert(User user) {
		users.compute(user.id(), (k, v) -> {
			if (v != null) {
				throw BaseException.exception(409, "user already exists");
			}
			return new User(user.id(), user.password(), user.name(), user.email());
		});
		return user.id();
	}

	@Override
	public List<User> findAll() {
		return new ArrayList<>(users.values());
	}

	@Override
	public Optional<User> findById(String id) {
		return Optional.ofNullable(users.get(id));
	}

	@Override
	public void update(String s, User user) {
		users.put(s, user);
	}
}
