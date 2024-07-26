package org.example.config.conatiner;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.example.config.HttpMethod;
import org.example.config.annotation.Component;
import org.example.config.annotation.Controller;
import org.example.config.annotation.PathVariable;
import org.example.config.annotation.RequestMapping;
import org.example.config.annotation.RequestParam;
import org.example.config.controller.MethodHandler;
import org.example.config.controller.ParameterInfo;
import org.example.config.handler.AnnotationHandlerMapping.HandlerKey;
import org.example.config.handler.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class SimpleApplicationContext implements ApplicationContext, ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(SimpleApplicationContext.class);

    private final BeanContainer beanContainer;
    private final ClassScanner classScanner;
    private final AnnotationScanner annotationScanner;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            initialize("org.example");
            ServletContext servletContext = sce.getServletContext();
            servletContext.setAttribute("applicationContext", this);
        } catch (Exception e) {
            logger.info("Failed to initialize application context", e);
            throw new RuntimeException(e);
        }
    }

    public SimpleApplicationContext() {
        this.beanContainer = new BeanContainer();
        this.classScanner = new SimpleClassScanner();
        this.annotationScanner = new SimpleAnnotationScanner();
    }

    @Override
    public void initialize(Class<?> mainClass) throws Exception {
        String basePackage = mainClass.getPackage().getName();
        initialize(basePackage);
    }

    @Override
    public void initialize(String basePackage) throws Exception {
        logger.info("Initializing application context with base package: {}", basePackage);
        Set<Class<?>> scannedClasses = classScanner.scanPackage(basePackage);
        Set<Class<?>> beanClasses = findBeanClasses(scannedClasses);
        registerBeans(beanClasses);
        beanContainer.instantiateAndRegister();

        initHandler(scannedClasses);

        logger.info("Application context initialization completed");
    }

    private void initHandler(Set<Class<?>> scannedClasses)
            throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        logger.info("Initializing handler");
        HandlerMapping handlerMapping = beanContainer.getBean(HandlerMapping.class);
        Field handlerMap = handlerMapping.getClass().getDeclaredField("handlerMap");
        handlerMap.setAccessible(true);

        Map<HandlerKey, MethodHandler> currentHandlerMap = new HashMap<>();

        for (Object bean : beanContainer.getBeans().values()) {
            Class<?> clazz = bean.getClass();
            if (clazz.isAnnotationPresent(Controller.class)) {
                String baseUrl = "";
                HttpMethod classDefaultMethod = HttpMethod.GET;
                if (clazz.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping classMapping = clazz.getAnnotation(RequestMapping.class);
                    baseUrl = classMapping.path();
                    classDefaultMethod = classMapping.method();
                }

                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
                        String url = baseUrl + methodMapping.path();
                        HttpMethod httpMethod =
                                methodMapping.method() != HttpMethod.GET ? methodMapping.method() : classDefaultMethod;

                        HandlerKey key = new HandlerKey(url, httpMethod);
                        MethodHandler methodHandler = new MethodHandler(bean, method);

                        // PathVariable과 RequestParam 정보 추가
                        Map<String, Class<?>> pathVariables = new HashMap<>();
                        Map<String, ParameterInfo> requestParams = new HashMap<>();

                        Parameter[] parameters = method.getParameters();
                        for (Parameter parameter : parameters) {
                            if (parameter.isAnnotationPresent(PathVariable.class)) {
                                PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
                                String name =
                                        pathVariable.value().isEmpty() ? parameter.getName() : pathVariable.value();
                                pathVariables.put(name, parameter.getType());
                            } else if (parameter.isAnnotationPresent(RequestParam.class)) {
                                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                                String name =
                                        requestParam.value().isEmpty() ? parameter.getName() : requestParam.value();
                                requestParams.put(name, new ParameterInfo(parameter.getType(), requestParam.required(),
                                        requestParam.defaultValue()));
                            }
                        }

                        methodHandler.setPathVariables(pathVariables);
                        methodHandler.setRequestParams(requestParams);

                        currentHandlerMap.put(key, methodHandler);
                        logger.info("Mapped \"{}\" onto {}", key, method);
                    }
                }
            }
        }
        handlerMap.set(handlerMapping, currentHandlerMap);
        logger.info("HandlerMap : {}", currentHandlerMap);
        logger.info("Initializing handlers completed");
    }


    private Set<Class<?>> findBeanClasses(Set<Class<?>> scannedClasses) {
        return annotationScanner.findAnnotatedClasses(scannedClasses,
                Component.class,
                Controller.class);
    }

    private void registerBeans(Set<Class<?>> beanClasses) {
        beanClasses.forEach(beanClass -> {
            String beanName = toBeanName(beanClass.getSimpleName());
            beanContainer.registerBeanClass(beanName, beanClass);
            logger.debug("Registered bean class: {} with name: {}", beanClass.getName(), beanName);
        });
    }

    @Override
    public <T> T getBean(String name) {
        return (T) beanContainer.getBean(name);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return (T) beanContainer.getBean(clazz);
    }

    private String toBeanName(String className) {
        return Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }
}