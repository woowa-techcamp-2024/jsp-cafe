package com.woowa.hyeonsik.domain;

public record Article(Long id, String writer, String title, String contents) {
    private static final int WRITER_MAX_LENGTH = 30;
    private static final int TITLE_MAX_LENGTH = 30;
    private static final int CONTENT_MAX_LENGTH = 200;

    public Article(Long id, String writer, String title, String contents) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        validate();
    }

    private void validate() {
        if (writer == null || writer.trim().isEmpty()) {
            throw new IllegalArgumentException("작성자는 필수 입력 항목입니다.");
        }
        if (writer.length() > WRITER_MAX_LENGTH) {
            throw new IllegalArgumentException("작성자 이름은 " + WRITER_MAX_LENGTH + "자를 초과할 수 없습니다.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 필수 입력 항목입니다.");
        }
        if (title.length() > TITLE_MAX_LENGTH) {
            throw new IllegalArgumentException("제목은 " + TITLE_MAX_LENGTH + "자를 초과할 수 없습니다.");
        }
        if (contents == null || contents.trim().isEmpty()) {
            throw new IllegalArgumentException("내용은 필수 입력 항목입니다.");
        }
        if (contents.length() > CONTENT_MAX_LENGTH) {
            throw new IllegalArgumentException("내용은 " + CONTENT_MAX_LENGTH + "자를 초과할 수 없습니다.");
        }
    }
}
