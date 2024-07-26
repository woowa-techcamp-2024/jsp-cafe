package woopaca.jspcafe.fixture;

import woopaca.jspcafe.model.User;

import java.time.LocalDate;

public class UserFixture {

    public static User testUser() {
        return new User(111L, "test@email.com", "test", "test", LocalDate.now());
    }
}
