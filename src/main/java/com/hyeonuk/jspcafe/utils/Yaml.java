package com.hyeonuk.jspcafe.utils;

import java.util.HashMap;
import java.util.Map;

public class Yaml {
    private Map<String,Object> map = new HashMap<>();
    private final String path;
    public Yaml(Map<String,Object> map,String path){
        this.map = map;
        this.path = path;
    }

    public Object get(String key) {
        String[] keys = key.split("\\.");
        Map<String, Object> currentMap = map;
        for (int i = 0; i < keys.length - 1; i++) {
            Object value = currentMap.get(keys[i]);
            if (value instanceof Map) {
                currentMap = (Map<String, Object>) value;
            } else {
                return null;
            }
        }
        return currentMap.get(keys[keys.length - 1]);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getMap(String key) {
        Object value = get(key);
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        }
        return null;
    }

    public String getString(String key) {
        Object value = get(key);
        if(value == null || !(value instanceof String)){
            throw new IllegalArgumentException(path+" 안에 "+key+"값이 존재하지 않습니다.");
        }
        String v = (String) value;
        if(v.isEmpty()){
            throw new IllegalArgumentException(path+" 안에 "+key+"값이 존재하지 않습니다.");
        }
        return v;
    }
}
