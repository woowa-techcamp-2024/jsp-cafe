package woowa.frame.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanContainer {

    private static BeanContainer INSTANCE = new BeanContainer();

    public static BeanContainer getInstance() {
        return INSTANCE;
    }

    private BeanContainer() { }

    private final Logger logger = LoggerFactory.getLogger(BeanContainer.class);
    private final Map<Class<?>, Object> beans = new HashMap<>();

    /**
     * 생성하고자하는 빈의 클래스를 등록합니다
     *
     * @param classes 등록하고자하는 빈의 클래스
     */
    public void registerBean(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            beans.put(clazz, null);
        }
    }

    /**
     * 초기화한 빈을 등록합니다.
     *
     * @param bean 등록하고자하는 빈 객체
     */
    public void registerBeanObject(Object bean) {
        beans.put(bean.getClass(), bean);
    }

    /**
     * 현재 등록된 빈을 생성합니다.
     */
    public void createBeans() {
        for (Class<?> clazz : beans.keySet()) {
            getBean(clazz);
        }
    }

    /**
     * 빈 컨테이너에서 등록된 빈을 조회합니다. 만약 빈이 없다면 예외가 발생합니다.
     * 하지만 아직 초기화 작업이 되지 않은 빈이라면 예외를 발생시키지 않고 새로 빈을 생성하여 등록해줍니다.
     *
     * @param clazz 컨테이너에서 조회할 빈의 클래스 타입
     * @param <T>   빈의 클래스 타입
     * @return 빈 객체
     */
    public <T> T getBean(Class<T> clazz) {
        T bean = (T) beans.get(clazz);

        if (bean != null) {
            return bean;
        }

        if (!beans.containsKey(clazz)) {
            throw new RuntimeException("빈으로 등록되진 않은 클래스입니다.");
        }

        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length == 1) {

            Constructor<T> constructor = (Constructor<T>) constructors[0];
            Parameter[] args = constructor.getParameters();

            List<Object> parameters = new ArrayList<>();
            for (Parameter arg : args) {
                Object object = getBean(arg.getType());
                parameters.add(object);
            }

            try {
                bean = constructor.newInstance(parameters.toArray());
                beans.put(clazz, bean);
                return bean;
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                logger.error("빈으로 등록할 클래스의 생성자를 호출할 수 없습니다.");
                throw new RuntimeException("빈으로 등록할 클래스의 생성자를 호출할 수 없습니다.");
            }
        } else {
            logger.error("빈으로 등록할 클래스의 생성자는 하나이어야 합니다.");
            throw new RuntimeException("빈으로 등록할 클래스의 생성자는 하나이어야 합니다.");
        }
    }

    /**
     * 빈 컨테이너에 저장된 빈을 모두 조회합니다. 찾고자하는 클래스의 자식 클래스 타입의 빈까지 모두 조회하여 반환합니다.
     * 만약 찾는 빈이 존재하지 않는다면 빈 리스트를 반환합니다.
     *
     * @return
     */
    public <T> List<T> getBeans(Class<T> clazz) {
        List<T> beans = new ArrayList<>();
        for (Class<?> type : this.beans.keySet()) {
            if (clazz.isAssignableFrom(type)) {
                beans.add((T) this.beans.get(type));
            }
        }
        return beans;
    }

    /**
     * 빈 컨테이너에서 해당 어노테이션을 가지는 빈 객체를 모두 조회합니다.
     *
     * @param annotation 어노테이션 조건
     * @return 어노테이션을 가지는 빈 객체 리스트
     */
    public List<Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {
        List<Object> beans = new ArrayList<>();
        for (Class<?> type : this.beans.keySet()) {
            if (type.isAnnotationPresent(annotation)) {
                beans.add(getBean(type));
            }
        }
        return beans;
    }
}
