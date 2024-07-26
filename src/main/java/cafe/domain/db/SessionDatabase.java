package cafe.domain.db;

import cafe.domain.entity.User;
import cafe.dto.UserDto;

import java.util.HashMap;
import java.util.Map;

public class SessionDatabase implements Database {
    private final Map<String, User> sessions;

    public SessionDatabase() {
        this.sessions = new HashMap<>();
    }

    @Override
    public void insert(Object data) {
        if (!(data instanceof UserDto)) {
            throw new IllegalArgumentException("User 타입만 저장 가능합니다.");
        }
        UserDto userDto = (UserDto) data;
        sessions.put(userDto.getId(), userDto.getUser());
    }

    @Override
    public Object selectById(Object id) {
        if (!(id instanceof String)) {
            throw new IllegalArgumentException("String 타입만 조회 가능합니다.");
        }
        return sessions.get(id);
    }

    @Override
    public Map<String, User> selectAll() {
        return sessions;
    }

    @Override
    public void deleteById(Object id) {
        if (!(id instanceof String)) {
            throw new IllegalArgumentException("String 타입만 삭제 가능합니다.");
        }
        sessions.remove(id);
    }
}
