package cafe.dto;

import cafe.domain.entity.User;

public class UserDto {
    private final String id;
    private final User user;

    public UserDto(String id, User user) {
        this.id = id;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }
}
