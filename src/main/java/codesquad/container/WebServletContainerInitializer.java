package codesquad.container;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;


public class WebServletContainerInitializer implements ServletContainerInitializer {
    private static final Logger logger = LoggerFactory.getLogger(WebServletContainerInitializer.class);

    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        logger.info("AppInitializer onStartup...");
        logger.info("Class = {}", set);
        logger.info("ServletContext = {}", servletContext);

//        for (Class<?> appInitClass : set) {
//            try {
//                AppInit appInit = (AppInit) appInitClass.getConstructor().newInstance();
//                appInit.onStartUp(servletContext);
//            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
//                     InvocationTargetException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }
}
