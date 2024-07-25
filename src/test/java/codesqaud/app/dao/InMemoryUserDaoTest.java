package codesqaud.app.dao;

import codesqaud.app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class InMemoryUserDaoTest {
    private InMemoryUserDao userDao;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        userDao = new InMemoryUserDao();

        user1 = new User("semin1", "password1", "semin1", "semin1@example.com");
        user2 = new User("semin2", "password2", "semin2", "semin2@example.com");
        user3 = new User("semin3", "password3", "semin3", "semin3@example.com");
    }

    @Nested
    class Save_할_때 {
        @Test
        void 저장된_사용자가_없으면_생성한다() {
            userDao.save(user1);
            Optional<User> findUser = userDao.findById(user1.getId());

            assertThat(findUser).isPresent();
            assertThat(findUser.get().getName()).isEqualTo("semin1");
        }

        @Test
        void 저장된_사용자가_있으면_업데이트한다() {
            userDao.save(user1);
            user1.setName("newName");

            userDao.save(user1);
            List<User> users = userDao.findAll();
            assertThat(users).hasSize(1);

            Optional<User> findUser = userDao.findById(user1.getId());

            assertThat(findUser).isPresent();
            assertThat(findUser.get().getName()).isEqualTo("newName");
        }
    }

    @Nested
    class FindAll_할_때 {
        @Test
        void 모든_사용자를_검색할_수_있다() {
            userDao.save(user1);
            userDao.save(user2);
            userDao.save(user3);
            List<User> users = userDao.findAll();

            assertThat(users).hasSize(3)
                    .extracting(User::getName).containsExactlyInAnyOrder("semin1", "semin2", "semin3");
        }
    }

    @Nested
    class Delete_할_때 {
        @Test
        void 저장한_사용자를_삭제할_수_있다() {
            userDao.save(user1);
            userDao.delete(user1);
            Optional<User> findUser = userDao.findById(user1.getId());

            assertThat(findUser).isNotPresent();
        }

        @Test
        void 저장하지_않은_사용자를_삭제하려하면_예외가_발생한다() {
            assertThatThrownBy(() -> userDao.delete(user1))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class FindByUserId_할_때 {
        @Test
        @DisplayName("should find user by userId")
        void 저장한_사용자를_검색할_수_있다() {
            userDao.save(user1);
            Optional<User> retrievedUser = userDao.findByUserId(user1.getUserId());

            assertThat(retrievedUser).isPresent();
            assertThat(retrievedUser.get().getName()).isEqualTo(user1.getUserId());
        }

        @Test
        void testFindByUserIdNotFound() {
            Optional<User> retrievedUser = userDao.findByUserId("nonexistent");

            assertThat(retrievedUser).isNotPresent();
        }
    }
}
