package org.example.member.service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import org.example.member.model.dto.UserResponseDto;
import org.example.member.repository.UserRepository;

public class UserQueryService {

    private final UserRepository userRepository;

    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDto> findAllUsers() throws SQLException {
        return userRepository.findAllUsers()
                .stream().map(UserResponseDto::toResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public UserResponseDto findUserByUserId(String userId) throws SQLException {
        return UserResponseDto.toResponse(userRepository.findUserByUserId(userId));
    }
}
