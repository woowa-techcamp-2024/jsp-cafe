package com.codesquad.cafe.db.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codesquad.cafe.db.domain.User;

public class InMemoryUserRepository implements UserRepository {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Map<Long, User> users;

	private final AtomicLong seq;

	public InMemoryUserRepository() {
		users = new ConcurrentHashMap<>();
		seq = new AtomicLong(1);
	}

	@Override
	public User save(User user) {
		if (user.getId() != null) {
			return update(user);
		} else {
			return create(user);
		}
	}

	private User update(User user) {
		if (user.getId() == null) {
			throw new IllegalArgumentException("존재하지 않는 user 입니다.");
		}
		users.put(user.getId(), user);
		return user;
	}

	private User create(User user) {
		if (user.getId() != null) {
			throw new IllegalArgumentException("이미 존재하는 user 입니다.");
		}
		findByUsername(user.getUsername()).ifPresent(existUser -> {
			log.debug("unique constraint violated: {}", user.getUsername());
			throw new IllegalArgumentException("이미 존재하는 username 입니다.");
		});
		user.setId(seq.getAndIncrement());
		users.put(user.getId(), user);
		return user;
	}

	@Override
	public Optional<User> findById(Long id) {
		return Optional.ofNullable(users.getOrDefault(id, null));
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return users.entrySet().stream()
			.filter(user -> user.getValue().getUsername().equals(username))
			.findFirst()
			.map(Map.Entry::getValue);
	}

	@Override
	public List<User> findAll() {
		return users.values()
			.stream()
			.toList();
	}

	@Override
	public void deleteAll() {
		this.users.clear();
	}

}
