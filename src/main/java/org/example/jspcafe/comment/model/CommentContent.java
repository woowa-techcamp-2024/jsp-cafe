package org.example.jspcafe.comment.model;

public class CommentContent {
    private final String value;
    public String getValue() {
        return value;
    }

    public CommentContent(String comment) {
        validateContent(comment);
        validateLength(comment);
        this.value = comment;
    }

    private void validateContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("내용이 없습니다.");
        }
    }

    private void validateLength(String content) {
        if (content.length() > 200) {
            throw new IllegalArgumentException("댓글 내용은 200 이하여야 합니다.");
        }
    }
}
