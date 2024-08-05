package codesquad.global.container.initializer;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.annotation.HandlesTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@HandlesTypes(AppInit.class)
public class WebServletContainerInitializer implements ServletContainerInitializer {
    private static final Logger logger = LoggerFactory.getLogger(WebServletContainerInitializer.class);

    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        logger.info("AppInitializer onStartup...");

        ServletRegistration.Dynamic jspServlet = servletContext.addServlet("jsp", "org.apache.jasper.servlet.JspServlet");
        jspServlet.setLoadOnStartup(1);
        jspServlet.addMapping("*.jsp");
        jspServlet.addMapping("/WEB-INF/*");

        List<AppInit> appInitList = new ArrayList<>();
        for (Class<?> appInitClass : set) {
            try {
                AppInit appInit = (AppInit) appInitClass.getConstructor().newInstance();
                appInitList.add(appInit);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        appInitList.sort(Comparator.comparing(AppInit::order));
        for (AppInit appInit : appInitList) {
            try {
                appInit.onStartUp(servletContext);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        logger.info("AppInitializer onStartup completed...");
    }
}
