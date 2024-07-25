package codesqaud.app.dao;

import codesqaud.app.exception.HttpException;
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

    @BeforeEach
    public void setUp() {
        userDao = new InMemoryUserDao();

        user1 = new User("semin1", "password1", "semin1", "semin1@example.com");
        user2 = new User("semin2", "password2", "semin2", "semin2@example.com");
    }


    @Nested
    class 사용자_저장_할_때 {
        @Test
        void 저장된_사용자가_없으면_생성한다() {
            userDao.save(user1);
            Optional<User> findUser = userDao.findById(user1.getId());

            assertThat(findUser).isPresent();
            assertThat(findUser.get().getName()).isEqualTo("semin1");
        }
    }

    @Nested
    class 모든_사용자_검색_할_때 {
        @Test
        void 모든_사용자를_검색할_수_있다() {
            userDao.save(user1);
            userDao.save(user2);
            List<User> users = userDao.findAll();

            assertThat(users).hasSize(2)
                    .extracting(User::getName).containsExactlyInAnyOrder("semin1", "semin2");
        }
    }

    @Nested
    class 사용자_삭제_할_때 {
        @Test
        void 저장한_사용자를_삭제할_수_있다() {
            userDao.save(user1);
            userDao.delete(user1);
            Optional<User> findUser = userDao.findById(user1.getId());

            assertThat(findUser).isNotPresent();
        }

        @Test
        void 저장하지_않은_사용자를_삭제하려하면_예외가_발생한다() {
            user1.setId(1L);  // 기존에 저장되지 않은 사용자
            assertThatThrownBy(() -> userDao.delete(user1))
                    .isInstanceOf(HttpException.class);
        }
    }

    @Nested
    class 사용자_아이디로_검색_할_때 {
        @Test
        void 저장한_사용자를_검색할_수_있다() {
            userDao.save(user1);
            Optional<User> retrievedUser = userDao.findByUserId(user1.getUserId());

            assertThat(retrievedUser).isPresent();
            assertThat(retrievedUser.get().getName()).isEqualTo("semin1");
        }

        @Test
        void 존재하지_않는_사용자를_검색하려하면_빈_옵셔널을_반환한다() {
            Optional<User> retrievedUser = userDao.findByUserId("nonexistent");

            assertThat(retrievedUser).isNotPresent();
        }
    }

    @Nested
    class 사용자_업데이트_할_때 {
        @Test
        void 존재하는_사용자를_업데이트할_수_있다() {
            userDao.save(user1);

            User updatedUser = new User("semin1_updated", "password1_updated", "semin1_updated", "semin1_updated@example.com");
            updatedUser.setId(user1.getId());

            userDao.update(updatedUser);

            Optional<User> findUser = userDao.findById(user1.getId());
            assertThat(findUser).isPresent();
            User result = findUser.get();
            assertThat(result.getUserId()).isEqualTo("semin1_updated");
            assertThat(result.getPassword()).isEqualTo("password1_updated");
            assertThat(result.getName()).isEqualTo("semin1_updated");
            assertThat(result.getEmail()).isEqualTo("semin1_updated@example.com");
        }

        @Test
        void 존재하지_않는_사용자를_업데이트하려하면_예외가_발생한다() {
            User nonExistentUser = new User("semin_nonexistent", "password", "semin_nonexistent", "nonexistent@example.com");
            nonExistentUser.setId(999L);

            assertThatThrownBy(() -> userDao.update(nonExistentUser))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("업데이트 할 사용자를 찾지 못했습니다.");
        }

        @Test
        void 사용자_ID가_null인_경우_예외가_발생한다() {
            assertThatThrownBy(() -> userDao.update(user1))
                    .isInstanceOf(HttpException.class);
        }
    }
}
