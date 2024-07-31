package com.woowa.framework;

import com.woowa.config.DatabaseConfig;
import com.woowa.config.HandlerConfig;
import com.woowa.config.LibraryConfig;
import com.woowa.framework.argumentresovler.ArgumentResolverConfig;
import com.woowa.framework.web.HandlerMappingConfig;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);
    private final Map<String, Object> context = new ConcurrentHashMap<>();

    public void start() {
        log.debug("컨테이너 실행");
        try {
            context.put("galaxyContainer", this);
            register(HandlerMappingConfig.class);
            register(ArgumentResolverConfig.class);
            register(LibraryConfig.class);
            register(DatabaseConfig.class);
            register(HandlerConfig.class);
        } catch (Exception e) {
            throw new ContainerInitializeException("리플렉션 에러 발생");
        }
    }

    private void register(Class<?> clazz)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        // 기본 생성자 획득
        Constructor<?> constructor = clazz.getConstructor();
        Object configObj = constructor.newInstance();

        // 퍼블릭 메서드 순회
        for (Method method : clazz.getMethods()) {
            if(!method.isAnnotationPresent(Bean.class)) {
                continue;
            }
            if(context.containsKey(method.getName())) {
                continue;
            }
            String beanName = method.getName();

            // 메서드 파라미터로 사용할 빈 조회
            Object[] parameters = new Object[method.getParameterCount()];
            Class<?>[] parameterTypes = method.getParameterTypes();
            for(int i=0; i<parameters.length; i++) {
                Object bean = getBean(parameterTypes[i]);
                parameters[i] = bean;
            }
            Object bean = method.invoke(configObj, parameters);
            context.put(beanName, bean);
        }
    }

    public <T> T getBean(Class<T> clazz) {
        return context.values().stream()
                .filter(obj -> isChild(obj.getClass(), clazz))
                .findFirst()
                .map(clazz::cast)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 빈입니다."));
    }

    private <T> boolean isChild(Class<?> subClass, Class<T> targetClass) {
        if(isImplementation(subClass, targetClass)) {
            return true;
        }

        if(subClass.equals(targetClass)) {
            return true;
        }
        if(subClass.equals(Object.class)) {
            return false;
        }
        return isChild(subClass.getSuperclass(), targetClass);
    }

    private <T> boolean isImplementation(Class<?> subClass, Class<T> targetClass) {
        for (Class<?> anInterface : subClass.getInterfaces()) {
            if(anInterface.equals(targetClass)) {
                return true;
            }
            if(isImplementation(anInterface, targetClass)) {
                return true;
            }
        }
        return false;
    }


    public List<Object> getBeans() {
        return context.values().stream().toList();
    }

    public <T> List<T> getBeans(Class<T> clazz) {
        return context.values().stream()
                .filter(bean -> isChild(bean.getClass(), clazz))
                .map(bean -> (T) bean)
                .toList();
    }
}
