package codesquad.javacafe.member.dto.request;

import codesquad.javacafe.member.entity.Member;

import java.util.Map;
import java.util.Objects;

public class MemberCreateRequestDto {
    private String userId;
    private String password;
    private String name;

    public MemberCreateRequestDto(Map<String, String[]> body) {
        this.userId = body.get("userId")[0];
        this.password = body.get("password")[0];
        this.name = body.get("name")[0];
    }


    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberCreateRequestDto that = (MemberCreateRequestDto) o;
        return Objects.equals(userId, that.userId) && Objects.equals(password, that.password) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, password, name);
    }
}
