package woowa.camp.jspcafe.service.dto;

import woowa.camp.jspcafe.domain.User;

public record UserResponse(Long id, String nickname, String email) {

    public static UserResponse of(User user) {
        return new UserResponse(user.getId(), user.getNickname(), user.getEmail());
    }

}
