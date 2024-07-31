package org.example.member.service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Component;
import org.example.member.model.dto.UserDto;
import org.example.member.repository.UserRepository;

@Component
public class UserQueryService {

    private final UserRepository userRepository;

    @Autowired
    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> findAllUsers() throws SQLException {
        return userRepository.findAllUsers()
                .stream().map(UserDto::toResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public UserDto findUserByUserId(String userId) throws SQLException {
        return UserDto.toResponse(userRepository.findUserByUserId(userId));
    }
}
