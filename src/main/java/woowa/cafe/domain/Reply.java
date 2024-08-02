package woowa.cafe.domain;

import java.time.LocalDateTime;

/**
 * 답변(Reply) 도메인
 * <p>
 * <li>id : primary key</li>
 * <li>content : 답변 내용</li>
 * <li>createdDate : 답변 생성일</li>
 * <li>status : 답변 상태</li>
 * <li>userId : 답변 작성자 id</li>
 * <li>authorName : 답변 작성자 이름</li>
 * <li>questionId : 답변이 달린 질문글 key</li>
 * </p>
 */
public class Reply {

    private long id;
    private String content;
    private LocalDateTime createdDate;
    private String status;

    private String userId;
    private String authorName;

    private String questionId;

    public Reply(String content, String status, String userId, String authorName, String questionId) {
        this.content = content;
        this.createdDate = LocalDateTime.now();
        this.status = status;
        this.userId = userId;
        this.authorName = authorName;
        this.questionId = questionId;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public String getStatus() {
        return status;
    }

    public String getUserId() {
        return userId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getQuestionId() {
        return questionId;
    }
}
