package woopaca.jspcafe.servlet.dto;

import woopaca.jspcafe.model.User;

public record MembersResponse(String nickname, String email, String createdAt) {

    public static MembersResponse from(User user) {
        return new MembersResponse(user.getNickname(), user.getUsername(), user.getCreatedAt().toString());
    }
}
