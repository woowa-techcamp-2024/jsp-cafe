package camp.woowa.jspcafe.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceLocator {
    public static final Map<Class<?>, Object> services = new ConcurrentHashMap<>();

    private ServiceLocator() {}

    public static <T> void registerService(Class<T> clazz, T service) {
        services.put(clazz, service);
    }

    public static <T> T getService(Class<T> clazz) {
        return clazz.cast(services.get(clazz));
    }
}
