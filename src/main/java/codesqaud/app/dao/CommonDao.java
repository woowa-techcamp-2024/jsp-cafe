package codesqaud.app.dao;

import java.util.List;
import java.util.Optional;

public interface CommonDao <P, T> {
    void save(P p);
    Optional<T> findById(P p);
    List<T> findAll();
    void delete(P p);
}
