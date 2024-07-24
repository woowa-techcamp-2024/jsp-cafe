package com.example.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.example.entity.User;

public class UserDatabase implements Database<String, User> {

	private final Map<String, User> users = new ConcurrentHashMap<>();

	@Override
	public void insert(User user) {
		users.compute(user.id(), (k, v) -> {
			if (v != null) {
				throw new IllegalArgumentException("duplicate userId: " + user.id());
			}
			return new User(user.id(), user.password(), user.name(), user.email());
		});
	}

	@Override
	public List<User> findAll() {
		return new ArrayList<>(users.values());
	}

	@Override
	public Optional<User> findById(String id) {
		return Optional.ofNullable(users.get(id));
	}
}
