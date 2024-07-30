package codesquad.javacafe.member.dto.response;

import codesquad.javacafe.member.entity.Member;

public class MemberResponseDto {
    private long id;
    private String userId;
    private String name;

    public MemberResponseDto(long id, String userId, String name) {
        this.id = id;
        this.userId = userId;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.userId = member.getUserId();
        this.name = member.getName();
    }

    @Override
    public String toString() {
        return "MemberResponseDto{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
