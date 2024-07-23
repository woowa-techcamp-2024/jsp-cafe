package woopaca.jspcafe.servlet.dto;

import woopaca.jspcafe.model.User;

import java.time.format.DateTimeFormatter;

public record UserProfile(String nickname, String email, String signUpAt, String password) {

    public static UserProfile from(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일에 가입");
        return new UserProfile(user.getNickname(), user.getUsername(), user.getCreatedAt().format(formatter), user.getPassword());
    }
}
