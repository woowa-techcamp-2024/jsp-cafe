package codesquad.javacafe.member.dto.request;

import codesquad.javacafe.member.entity.Member;

import java.util.Map;
import java.util.Objects;

public class MemberUpdateRequestDto {
    private String userId;
    private String oldPassword;
    private String password;
    private String name;

    public MemberUpdateRequestDto(Map<String, String[]> body) {
        this.userId = body.get("userId")[0];
        this.oldPassword = body.get("oldPassword")[0];
        this.password = body.get("password")[0];
        this.name = body.get("name")[0];
    }

    public String getUserId() {
        return userId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Member toEntity() {
        return new Member(this.userId, this.password, this.name);
    }

    @Override
    public String toString() {
        return "MemberUpdateRequestDto{" +
                "userId='" + userId + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
