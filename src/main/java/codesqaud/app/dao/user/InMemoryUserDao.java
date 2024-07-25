package codesqaud.app.dao.user;

import codesqaud.app.exception.HttpException;
import codesqaud.app.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class InMemoryUserDao implements UserDao {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserDao.class);

    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    private final Map<String, Long> userIdIndex = new ConcurrentHashMap<>();
    private final Map<Long, User> users = new ConcurrentHashMap<>();

    @Override
    public void save(User user) {
        user.setId(ID_GENERATOR.incrementAndGet());
        users.compute(user.getId(), (id, existingUser) -> {
            userIdIndex.put(user.getUserId(), user.getId());
            return user;
        });
    }

    @Override
    public void update(User user) {
        if (user.getId() == null) {
            log.error("업데이트할 모델에 id를 지정하지 않았습니다.");
            throw new HttpException(SC_INTERNAL_SERVER_ERROR);
        }

        users.compute(user.getId(), (id, existingUser) -> {
            if (existingUser == null) {
                log.info("업데이트 할 모델을 찾지 못했습니다.");
                throw new HttpException(SC_NOT_FOUND, "업데이트 할 사용자를 찾지 못했습니다.");
            }
            return user;
        });
    }

    @Override
    public Optional<User> findById(Long PK) {
        return Optional.ofNullable(users.get(PK));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void delete(User user) {
        if(user.getId() == null) {
            log.error("삭제 할 모델에 id를 지정하지 않았습니다.");
            throw new HttpException(SC_INTERNAL_SERVER_ERROR);
        }

        if (!userIdIndex.containsKey(user.getUserId())) {
            throw new HttpException(SC_NOT_FOUND, "해당 사용자는 존재하지 않습니다.");
        }
        userIdIndex.remove(user.getUserId());
        users.remove(user.getId());
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        if (!userIdIndex.containsKey(userId)) {
            return Optional.empty();
        }

        Long id = userIdIndex.get(userId);
        return Optional.of(users.get(id));
    }
}
