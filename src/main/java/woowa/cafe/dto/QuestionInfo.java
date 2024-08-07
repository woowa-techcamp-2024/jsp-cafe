package woowa.cafe.dto;

import java.time.LocalDateTime;

public record QuestionInfo(
        String id,
        String authorName,
        String title,
        String content,
        LocalDateTime createdAt
) {
}
