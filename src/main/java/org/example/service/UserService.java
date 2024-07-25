package org.example.service;

import java.util.List;
import java.util.Optional;
import org.example.dto.UserCreateReqDto;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.repository.UserRepositoryMemoryImpl;

public class UserService {

    private final UserRepository userRepository = UserRepositoryMemoryImpl.getInstance();

    public void createUser(
        UserCreateReqDto userCreateReqDto
    ) {

        userRepository.saveUser(userCreateReqDto.toEntity());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(String userId) {
        return userRepository.getUserByUserId(userId);
    }

    public boolean login(
        String userId,
        String password
    ) {
        Optional<User> user = userRepository.getUserByUserId(userId);
        return user.isPresent() && user.get().getPassword().equals(password);
    }
}
