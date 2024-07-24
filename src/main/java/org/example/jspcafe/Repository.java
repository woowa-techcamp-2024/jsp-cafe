package org.example.jspcafe;

import java.util.Optional;

public interface Repository<T> {
    T save(T entity);
    Optional<T> findById(Long id);
    void delete(T entity);
    void update(T entity);
}
