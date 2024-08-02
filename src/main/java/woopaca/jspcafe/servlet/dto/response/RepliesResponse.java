package woopaca.jspcafe.servlet.dto.response;

import woopaca.jspcafe.model.Reply;

import java.time.format.DateTimeFormatter;

public record RepliesResponse(Long id, String content, String writer, String writtenAt) {

    public static RepliesResponse of(Reply reply, String writer) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return new RepliesResponse(reply.getId(), reply.getContent(), writer, reply.getWrittenAt().format(formatter));
    }
}
