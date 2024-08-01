package codesquad.global.container.listener;

import codesquad.article.service.DeleteArticleService;
import codesquad.common.db.transaction.JdbcTransactionManager;
import codesquad.common.db.transaction.TransactionProxy;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

/**
 * 하나의 트랜잭션으로 처리되어야 하는 Service를 Proxy 객체로 묶기 위한 Listener
 */
public class TransactionProxyContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(TransactionProxyContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        // 등록된 TransactionManager 조회
        JdbcTransactionManager jdbcTransactionManager = (JdbcTransactionManager) servletContext.getAttribute("JdbcTransactionManager");
        // 등록된 DeleteArticleService를 TxProxy로 래핑
        DeleteArticleService target = (DeleteArticleService) servletContext.getAttribute("DeleteArticleService");
        DeleteArticleService proxyService = (DeleteArticleService) Proxy.newProxyInstance(DeleteArticleService.class.getClassLoader(), new Class[]{DeleteArticleService.class}, new TransactionProxy(target, jdbcTransactionManager));
        servletContext.setAttribute("DeleteArticleService", proxyService);
        logger.info("TxProxyContextListener contextInitialized");
    }
}
