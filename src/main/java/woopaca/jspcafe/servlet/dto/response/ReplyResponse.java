package woopaca.jspcafe.servlet.dto.response;

import woopaca.jspcafe.model.Reply;

import java.time.format.DateTimeFormatter;

public record ReplyResponse(Long id, String content, String writer, String writtenAt) {

    public static ReplyResponse of(Reply reply, String writer) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return new ReplyResponse(reply.getId(), reply.getContent(), writer, reply.getWrittenAt().format(formatter));
    }
}
