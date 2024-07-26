package woopaca.jspcafe.servlet.dto.response;

import woopaca.jspcafe.model.User;

public record MembersResponse(Long id, String nickname, String email, String createdAt) {

    public static MembersResponse from(User user) {
        return new MembersResponse(user.getId(), user.getNickname(), user.getUsername(), user.getCreatedAt().toString());
    }
}
