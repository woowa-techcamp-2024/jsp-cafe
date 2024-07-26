package woowa.camp.jspcafe.service.dto;

import java.time.LocalDate;
import woowa.camp.jspcafe.domain.User;

public class UserResponse {

    private Long id;
    private String nickname;
    private String email;
    private LocalDate registerAt;

    private UserResponse(Long id, String nickname, String email, LocalDate registerAt) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.registerAt = registerAt;
    }

    public static UserResponse of(User user) {
        return new UserResponse(user.getId(), user.getNickname(), user.getEmail(), user.getRegisterAt());
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getRegisterAt() {
        return registerAt;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", registerAt=" + registerAt +
                '}';
    }
}
