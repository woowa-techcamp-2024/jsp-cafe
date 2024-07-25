package cafe.questions.repository;

class MemoryArticleRepositoryTest extends ArticleRepositoryTest {

    @Override
    protected ArticleRepository createArticleRepository() {
        return new MemoryArticleRepository();
    }
}