package codesqaud.app.dto;

import codesqaud.app.model.Reply;
import codesqaud.app.model.User;
import codesqaud.app.util.TimeUtils;

public class ReplyDto {
    private final Long id;
    private final String contents;
    private final Boolean activate;
    private final String createdAt;
    private final UserDto author;
    private boolean isMine;

    public ReplyDto(Long id, String contents, Boolean activate, String createdAt, UserDto author) {
        this.id = id;
        this.contents = contents;
        this.activate = activate;
        this.createdAt = createdAt;
        this.author = author;
    }

    public static ReplyDto from(Reply reply, User user) {
        return ReplyDto.builder()
                .id(reply.getId())
                .contents(reply.getContents())
                .activate(reply.getActivate())
                .createdAt(TimeUtils.toStringForUser(reply.getCreatedAt()))
                .author(UserDto.from(user))
                .build();
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public Boolean getActivate() {
        return activate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public UserDto getAuthor() {
        return author;
    }

    public boolean getIsMine() {
        return isMine;
    }

    public void setMine(Long loginUserId) {
        this.isMine = loginUserId.equals(author.getId());
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private Long id;
        private String contents;
        private Boolean activate;
        private String createdAt;
        private UserDto author;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder contents(String contents) {
            this.contents = contents;
            return this;
        }

        public Builder activate(Boolean activate) {
            this.activate = activate;
            return this;
        }

        public Builder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder author(UserDto author) {
            this.author = author;
            return this;
        }

        public ReplyDto build() {
            return new ReplyDto(id, contents, activate, createdAt, author);
        }
    }
}
