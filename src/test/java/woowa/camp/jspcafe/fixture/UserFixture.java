package woowa.camp.jspcafe.fixture;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import woowa.camp.jspcafe.domain.User;

public class UserFixture {

    public static User createUser1() {
        return new User("user1@naver.com",
                "user1_닉네임",
                "user1_비밀번호",
                null);
    }

    public static User createUser2() {
        return new User("user2@naver.com",
                "user2_닉네임",
                "user2_비밀번호",
                null);
    }

    public static List<User> createMultipleUsers(int count, LocalDate now) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(i -> createUser(count, now))
                .toList();
    }

    public static User createUser(int number, LocalDate now) {
        return new User("user" + number + "@naver.com", "user" + number + "_닉네임", "user" + number + "_비밀번호", now);
    }

}
