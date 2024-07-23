package camp.woowa.jspcafe.repository;

public interface QuestionRepository {
    Long save(Long id, String title, String content, String writer);
}
