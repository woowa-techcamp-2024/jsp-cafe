package woowa.camp.jspcafe.fixture;

import woowa.camp.jspcafe.domain.User;

public class UserFixture {

    public static User createUser1() {
        return new User("user1@naver.com", "user1_닉네임", "user1_비밀번호");
    }

    public static User createUser2() {
        return new User("user2@naver.com", "user2_닉네임", "user2_비밀번호");
    }

}
