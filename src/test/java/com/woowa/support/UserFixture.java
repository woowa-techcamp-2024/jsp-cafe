package com.woowa.support;

import com.woowa.model.User;
import java.util.UUID;

public class UserFixture {
    public static User user() {
        return User.create(UUID.randomUUID().toString(), "test@test.com", "test", "test");
    }
}
