package codesquad.javacafe.member.entity;

public class Member {
    private long id;
    private String userId;
    private String password;
    private String name;

    public Member(String userId, String password, String name) {
        this.userId = userId;
        this.password = password;
        this.name = name;
    }

    public long getId() {
        return id;
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
        return "Member{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
