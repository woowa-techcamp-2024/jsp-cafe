package org.example.cafe.application;

import java.util.List;
import org.example.cafe.application.dto.UserCreateDto;
import org.example.cafe.domain.User;
import org.example.cafe.domain.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(UserCreateDto userCreateDto) {
        User user = userCreateDto.toUser();
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(String id) {
        return userRepository.findById(id);
    }
}
