package cafe.users.repository;

public class MemoryUserRepositoryTest extends UserRepositoryTest {
    @Override
    protected UserRepository createUserRepository() {
        return new MemoryUserRepository();
    }
}
