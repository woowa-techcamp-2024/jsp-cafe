package org.example.cafe.infra.jdbc;

public class GeneratedKeyHolder implements KeyHolder {

    private Long key;

    @Override
    public Long getKey() {
        return key;
    }

    @Override
    public void setKey(Long id) {
        this.key = id;
    }
}
