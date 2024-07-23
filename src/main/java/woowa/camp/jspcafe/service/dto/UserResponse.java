package woowa.camp.jspcafe.service.dto;

import woowa.camp.jspcafe.domain.User;

public record UserResponse(Long id, String userId, String name, String email) {

    public static UserResponse of(User user) {
        return new UserResponse(user.getId(), user.getUserId(), user.getName(), user.getEmail());
    }

}
