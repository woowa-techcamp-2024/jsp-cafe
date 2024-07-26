package woowa.cafe.dto;

public record QuestionInfo(
        String id,
        String authorName,
        String title,
        String content
) {
    public String getPostTime() {
        return "2021-07-07 12:00:00";
    }
}
