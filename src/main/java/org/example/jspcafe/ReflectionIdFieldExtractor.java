package org.example.jspcafe;

import java.lang.reflect.Field;

public class ReflectionIdFieldExtractor<T> implements IdFieldExtractor<T> {
    private final Field idField;

    public ReflectionIdFieldExtractor(Class<T> clazz) {
        this.idField = findIdField(clazz);
        this.idField.setAccessible(true);
    }

    /**
     * 엔티티에 리플렉션을 이용하여 PK가 있는 필드에 ID를 할당합니다.
     *
     * @param id     할당할 ID
     * @param entity ID를 할당할 엔티티
     * @return 할당된 ID가 있는 엔티티
     */
    public T putId(Long id, T entity) {
        try {
            idField.set(entity, id);
            return entity;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Entity의 ID 필드에 접근할 수 없습니다.", e);
        }
    }


    @Override
    public Long getId(T entity) {
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

    private Field findIdField(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(PK.class)) {
                return field;
            }
        }
        throw new IllegalArgumentException("Entity 클래스에 @PK를 찾을 수 없습니다: " + clazz.getName());
    }
}
