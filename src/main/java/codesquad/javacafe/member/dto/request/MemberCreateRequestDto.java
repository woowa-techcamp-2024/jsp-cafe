package codesquad.javacafe.member.dto.request;

import codesquad.javacafe.member.entity.Member;

import java.util.Arrays;
import java.util.Map;

public class MemberCreateRequestDto {
    private String userId;
    private String password;
    private String name;

    public MemberCreateRequestDto(Map<String, String[]> body) {
        this.userId = body.get("userId")[0];
        this.password = body.get("password")[0];
        this.name = body.get("name")[0];
    }

    @Override
    public String toString() {
        return "MemberCreateRequestDto{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public Member toEntity() {
        return new Member(this.userId, this.password, this.name);
    }

}
