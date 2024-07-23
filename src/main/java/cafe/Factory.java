package cafe;

import cafe.users.MemoryUserRepository;
import cafe.users.UserRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class Factory {
    private final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();
    private final Map<Class<?>, Lock> locks = new ConcurrentHashMap<>();

    public UserRepository userRepository() {
        return getOrCreate(UserRepository.class, () -> new MemoryUserRepository());
    }

    protected <T> T getOrCreate(Class<T> beanClass, Supplier<T> supplier) {
        Lock lock = locks.computeIfAbsent(beanClass, k -> new ReentrantLock());
        lock.lock();
        try {
            if (!instances.containsKey(beanClass)) {
                T instance = supplier.get();
                instances.put(beanClass, instance);
                return instance;
            } else {
                return beanClass.cast(instances.get(beanClass));
            }
        } finally {
            lock.unlock();
        }
    }
}
