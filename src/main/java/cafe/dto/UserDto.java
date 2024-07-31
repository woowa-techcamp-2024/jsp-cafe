package cafe.dto;

import cafe.domain.entity.User;

public class UserDto {
    private final String id;
    private final User user;

    public UserDto(String id, User user) {
        if (id == null || user == null) {
            throw new IllegalArgumentException("인자에 null이 들어갈 수 없습니다.");
        }
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
