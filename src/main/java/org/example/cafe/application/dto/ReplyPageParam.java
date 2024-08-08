package org.example.cafe.application.dto;

import java.time.LocalDateTime;

public record ReplyPageParam(Long questionId,
                             Long lastReplyId,
                             LocalDateTime createdAt) {
}
