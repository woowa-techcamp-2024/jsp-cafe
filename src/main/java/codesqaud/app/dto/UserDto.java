package codesqaud.app.dto;

import codesqaud.app.model.User;

public record UserDto(Long id, String username, String name, String email) {
    public static UserDto fromUser(User user) {
        return new UserDto(
                user.getId(),
                user.getUserId(),
                user.getName(),
                user.getEmail()
        );
    }
}
