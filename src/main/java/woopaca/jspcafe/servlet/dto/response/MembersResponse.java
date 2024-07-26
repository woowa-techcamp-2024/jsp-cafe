package woopaca.jspcafe.servlet.dto.response;

import woopaca.jspcafe.model.User;

import java.time.format.DateTimeFormatter;

public record MembersResponse(Long id, String nickname, String email, String createdAt) {

    public static MembersResponse from(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return new MembersResponse(user.getId(), user.getNickname(), user.getUsername(), user.getCreatedAt().format(formatter));
    }
}
