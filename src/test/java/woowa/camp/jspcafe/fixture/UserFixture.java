package woowa.camp.jspcafe.fixture;

import woowa.camp.jspcafe.domain.User;

public class UserFixture {

    public static User createUser1() {
        return new User("user1_아이디", "user1_비밀번호", "user1_이름", "user1@naver.com");
    }

    public static User createUser2() {
        return new User("user2_아이디", "user2_비밀번호", "user2_이름", "user2@naver.com");
    }

}
