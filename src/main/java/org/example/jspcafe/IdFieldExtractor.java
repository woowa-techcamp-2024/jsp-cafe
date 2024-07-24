package org.example.jspcafe;

public interface IdFieldExtractor<T> {
    Long getId(T entity);
    T putId(Long id, T entity);
}
