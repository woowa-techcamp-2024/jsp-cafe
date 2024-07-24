package woowa.frame.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.frame.core.BeanContainer;
import woowa.frame.init.Initializer;
import woowa.frame.web.annotation.Router;
import woowa.frame.web.annotation.HttpMapping;

import java.lang.reflect.Method;
import java.util.List;

public class RouteInitializer implements Initializer {

    private final Logger logger = LoggerFactory.getLogger(RouteInitializer.class);

    /**
     * 등록된 Router 객체에서 Table 어노테이션이 붙은 메서드를 감지하여 라우팅 테이블에 추가합니다.
     *
     * @see Router
     * @see HttpMapping
     */
    @Override
    public void init(BeanContainer beanContainer) {

        RouteTable table = beanContainer.getBean(RouteTable.class);

        List<Object> routers = beanContainer.getBeansWithAnnotation(Router.class);
        for (Object router : routers) {
            Class<?> clazz = router.getClass();

            Router routerMeta = clazz.getAnnotation(Router.class);
            String rootUrl = routerMeta.url();

            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(HttpMapping.class)) {
                    HttpMapping mappingMeta = method.getAnnotation(HttpMapping.class);
                    String httpMethod = mappingMeta.method();
                    String subUrl = mappingMeta.urlTemplate();

                    RouteTableRow routeTableRow = new RouteTableRow(httpMethod, rootUrl + subUrl, router, method);
                    table.addRouteTableRow(routeTableRow);
                    logger.debug("add route table row: method={} url={}", httpMethod, rootUrl + subUrl);
                }
            }
        }
    }
}
