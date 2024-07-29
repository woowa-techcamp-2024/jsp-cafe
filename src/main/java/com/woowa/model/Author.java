package com.woowa.model;

import com.woowa.exception.AuthorizationException;
import java.util.Objects;

public class Author {
    private final String userId;
    private final String nickname;

    public Author(String userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

    public static Author from(User user) {
        return new Author(user.getUserId(), user.getNickname());
    }

    public String getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Author author = (Author) o;
        return Objects.equals(userId, author.userId) && Objects.equals(nickname, author.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, nickname);
    }

    @Override
    public String toString() {
        return "Author{" +
                "userId='" + userId + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }

    public void checkAuthority(User user) {
        if (user.getUserId().equals(userId)) {
            return;
        }
        throw new AuthorizationException("다른 사람의 질문은 수정할 수 없습니다.");
    }
}
