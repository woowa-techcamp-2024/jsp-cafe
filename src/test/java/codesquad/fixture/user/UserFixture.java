package codesquad.fixture.user;

import codesquad.domain.user.User;

public interface UserFixture {
    default User alice() {
        return new User(1L, "ALICE", "password_alice", "alice", "alice@gmail.com");
    }
}