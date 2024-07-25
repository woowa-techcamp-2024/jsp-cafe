package codesqaud.app.dao;

import java.util.List;
import java.util.Optional;

public interface CommonDao <TARGET, PK> {
    void save(TARGET TARGET);
    void update(TARGET TARGET);
    Optional<TARGET> findById(PK PK);
    List<TARGET> findAll();
    void delete(TARGET TARGET);
}
