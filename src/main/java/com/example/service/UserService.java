package com.example.service;

import java.util.Optional;

import com.example.db.UserDatabase;
import com.example.dto.LoginRequest;
import com.example.dto.SignupRequest;
import com.example.dto.UserUpdateRequest;
import com.example.entity.User;
import com.example.exception.BaseException;

public class UserService {

	private final UserDatabase userDatabase;

	public UserService(UserDatabase userDatabase) {
		this.userDatabase = userDatabase;
	}

	public void signup(SignupRequest request) {
		if (userDatabase.findById(request.id()).isPresent()) {
			throw BaseException.exception(409, "user already exists");
		}
		User user = new User(request.id(), request.password(), request.name(), request.email());
		userDatabase.insert(user);
	}

	public User login(LoginRequest request) {
		Optional<User> userOptional = userDatabase.findById(request.id());
		if (userOptional.isEmpty()) {
			throw BaseException.exception(400, "invalid auth");
		}
		User user = userOptional.get();
		if (!user.password().equals(request.password())) {
			throw BaseException.exception(400, "invalid auth");
		}
		return user;
	}

	public User getUser(String id) {
		Optional<User> userOptional = userDatabase.findById(id);
		if (userOptional.isEmpty()) {
			throw BaseException.exception(400, "user not found");
		}
		return userOptional.get();
	}

	public void updateUser(String id, UserUpdateRequest request) {
		Optional<User> userOptional = userDatabase.findById(id);
		if (userOptional.isEmpty()) {
			throw BaseException.exception(400, "user not found");
		}
		User user = userOptional.get();
		if (!user.id().equals(id)) {
			throw BaseException.exception(403, "not enough permissions");
		}
		if (!user.password().equals(request.password())) {
			throw BaseException.exception(400, "invalid password");
		}

		String updateName = request.name() == null ? user.name() : request.name();
		String updateEmail = request.email() == null ? user.email() : request.email();
		userDatabase.update(id, new User(null, null, updateName, updateEmail));
	}
}
