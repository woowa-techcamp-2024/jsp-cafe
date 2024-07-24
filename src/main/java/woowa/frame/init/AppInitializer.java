package woowa.frame.init;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.frame.core.BeanContainer;
import woowa.frame.core.annotation.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 커스텀 설정을 실행하는 첫 진입점입니다.
 * 패키지에 존재하는 초기화 클래스를 찾아서 실행합니다.
 */
@WebListener
public class AppInitializer implements ServletContextListener {

    private Logger logger = LoggerFactory.getLogger(AppInitializer.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        logger.info("start init");
        // scan class file
        Class<?>[] classes = ClassUtils.scan("woowa")
                .toArray(new Class[0]);


        // create bean
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.registerBeanObject(beanContainer);

        for (Class<?> clazz : classes) {
            if (clazz.isAnnotation() || clazz.isInterface()) continue;
            for (Annotation annotation : clazz.getAnnotations()) {
                // Component 어노테이션이 적용된 클래스를 등록한다.
                if (annotation.annotationType().equals(Component.class) ||
                    annotation.annotationType().isAnnotationPresent(Component.class)) {
                    logger.info("register bean name={}", clazz.getName());
                    beanContainer.registerBean(clazz);
                    break;
                }

            }
        }
        beanContainer.createBeans();

        // find init class
        List<Class<?>> initClasses = new ArrayList<>();
        for (Class clazz : classes) {
            if (Arrays.asList(clazz.getInterfaces()).contains(Initializer.class)) {
                initClasses.add(clazz);
            }
        }

        // init
        initClasses.forEach(clazz -> {
                    try {
                        Constructor<?> constructor = clazz.getConstructor();
                        Initializer initializer = (Initializer) constructor.newInstance();
                        initializer.init(beanContainer);
                        logger.info("init {}", constructor.getName());
                    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                             IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("application down....");
    }
}
