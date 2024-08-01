package codesquad.global.container.listener;

import codesquad.common.db.transaction.JdbcTransactionManager;
import codesquad.common.db.transaction.TxProxy;
import codesquad.service.DeleteArticleService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

public class TransactionProxyContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(TransactionProxyContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        JdbcTransactionManager jdbcTransactionManager = (JdbcTransactionManager) servletContext.getAttribute("jdbcTransactionManager");
        DeleteArticleService targetService = (DeleteArticleService) servletContext.getAttribute("deleteArticleService");
        DeleteArticleService proxyService = (DeleteArticleService) Proxy.newProxyInstance(DeleteArticleService.class.getClassLoader(), new Class[]{DeleteArticleService.class}, new TxProxy(targetService, jdbcTransactionManager));
        servletContext.setAttribute("deleteArticleService", proxyService);
        logger.info("TxProxyContextListener contextInitialized");
    }
}
