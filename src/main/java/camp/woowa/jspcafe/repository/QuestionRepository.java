package camp.woowa.jspcafe.repository;

public interface QuestionRepository {
    Long save(String title, String content, String writer);
}
