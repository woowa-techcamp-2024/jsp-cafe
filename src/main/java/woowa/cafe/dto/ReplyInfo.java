package woowa.cafe.dto;

import java.time.LocalDateTime;

public record ReplyInfo(
        long id,
        String content,
        LocalDateTime createdDate,
        String status,
        String userId,
        String authorName,
        String questionId
) {
}
