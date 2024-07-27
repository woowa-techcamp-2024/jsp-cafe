package woopaca.jspcafe.servlet.dto.response;

import woopaca.jspcafe.model.User;

import java.time.format.DateTimeFormatter;

public record UserProfile(Long id, String nickname, String email, String signUpAt) {

    public static UserProfile from(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일에 가입");
        return new UserProfile(user.getId(), user.getNickname(), user.getUsername(), user.getCreatedAt().format(formatter));
    }
}
