package org.example.jspcafe;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 메모리 기반 저장소 구현체
 * @param <T> 이 저장소에 저장되는 엔티티의 타입
 * InMemory를 사용하는 객체들은 모두 이 추상 클래스를 상속받아 구현합니다.
 */
public abstract class InMemoryRepository<T> implements Repository<T> {
    private final ConcurrentMap<Long, T> storage = new ConcurrentHashMap<>();
    protected final AtomicLong idGenerator = new AtomicLong(0);
    private final Field idField;

    /**
     * 엔티티를 저장소에 저장합니다.
     *
     * @param entity 저장할 엔티티
     * @return ID가 할당된 저장된 엔티티
     */
    @Override
    public T save(T entity) {
        validateEntity(entity);

        Long id = getId(entity);
        if(id == null) {
            id = idGenerator.incrementAndGet();
        }
        T savedEntity = storage.putIfAbsent(id, entity);
        if (savedEntity == null) {
            return putId(id, entity);
        }
        return savedEntity;
    }

    /**
     * 엔티티에 리플렉션을 이용하여 PK가 있는 필드에 ID를 할당합니다.
     *
     * @param id     할당할 ID
     * @param entity ID를 할당할 엔티티
     * @return 할당된 ID가 있는 엔티티
     */
    private T putId(Long id, T entity) {
        try {
            idField.set(entity, id);
            return entity;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Entity의 ID 필드에 접근할 수 없습니다.", e);
        }
    }

    /**
     * 리플렉션을 이용하여 엔티티의 ID를 가져옵니다.
     *
     * @param entity ID를 가져올 엔티티
     * @return 엔티티의 ID
     */
    protected Long getId(T entity) {
        try {
            Object value = idField.get(entity);
            if (value instanceof Long) {
                return (Long) value;
            }
            return null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Entity의 ID 필드에 접근할 수 없습니다.", e);
        }
    }
    /**
     * 주어진 ID로 엔티티를 찾습니다.
     *
     * @param id 찾을 엔티티의 ID
     * @return 주어진 ID의 엔티티, 없으면 null
     */
    @Override
    public T findById(Long id) {
        validateId(id);
        return storage.get(id);
    }

    /**
     * 엔티티를 저장소에서 삭제합니다.
     *
     * @param entity 삭제할 엔티티
     */
    @Override
    public void delete(T entity) {
        validateEntity(entity);
        Long id = getId(entity);
        validateId(id);
        storage.remove(id);
    }

    /**
     * 엔티티를 저장소에서 업데이트합니다.
     *
     * @param entity 업데이트할 엔티티
     */
    @Override
    public void update(T entity) {
        validateEntity(entity);
        Long id = getId(entity);
        validateId(id);
        storage.replace(id, entity);
    }

    /**
     * 엔티티가 null이 아닌지 검증합니다.
     *
     * @param entity 검증할 엔티티
     */
    private void validateEntity(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity는 null일 수 없습니다.");
        }
    }

    /**
     * ID가 null이 아닌지 검증합니다.
     *
     * @param id 검증할 ID
     */
    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id는 null일 수 없습니다.");
        }
    }

    protected InMemoryRepository(Class<T> clazz) {
        this.idField = findIdField(clazz);
        this.idField.setAccessible(true);
    }

    /**
     * 엔티티 클래스에서 @PK 어노테이션이 붙은 필드를 찾습니다.
     *
     * @param clazz 엔티티 클래스
     * @return @PK 어노테이션이 붙은 필드
     */
    private Field findIdField(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(PK.class)) {
                return field;
            }
        }
        throw new IllegalArgumentException("Entity 클래스에 @PK를 찾을 수 없습니다." + clazz.getName());
    }
}
