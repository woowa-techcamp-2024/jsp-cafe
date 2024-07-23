package com.woowa.model;

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
}
