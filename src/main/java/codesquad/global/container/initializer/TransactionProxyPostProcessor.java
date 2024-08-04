package codesquad.global.container.initializer;

import codesquad.article.service.DeleteArticleService;
import codesquad.comment.service.RegisterCommentService;
import codesquad.common.db.transaction.JdbcTransactionManager;
import codesquad.common.db.transaction.TransactionProxy;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

/**
 * 하나의 트랜잭션으로 처리되어야 하는 Service를 Proxy 객체로 묶기 위한 Initializer
 */
public class TransactionProxyPostProcessor implements AppInit {
    private static final Logger logger = LoggerFactory.getLogger(TransactionProxyPostProcessor.class);

    @Override
    public void onStartUp(ServletContext servletContext) throws Exception {
        // 등록된 TransactionManager 조회
        JdbcTransactionManager jdbcTransactionManager = (JdbcTransactionManager) servletContext.getAttribute("JdbcTransactionManager");
        // 등록된 DeleteArticleService를 TxProxy로 래핑
        DeleteArticleService deleteArticleService = (DeleteArticleService) servletContext.getAttribute("DeleteArticleService");
        DeleteArticleService deleteArticleServiceProxy = (DeleteArticleService) Proxy.newProxyInstance(DeleteArticleService.class.getClassLoader(), new Class[]{DeleteArticleService.class}, new TransactionProxy(deleteArticleService, jdbcTransactionManager));
        servletContext.setAttribute("DeleteArticleService", deleteArticleServiceProxy);
        // 등록된 RegisterCommentService를 TxProxy로 래핑
        RegisterCommentService registerCommentService = (RegisterCommentService) servletContext.getAttribute("RegisterCommentService");
        RegisterCommentService registerCommentServiceProxy = (RegisterCommentService) Proxy.newProxyInstance(RegisterCommentService.class.getClassLoader(), new Class[]{RegisterCommentService.class}, new TransactionProxy(registerCommentService, jdbcTransactionManager));
        servletContext.setAttribute("RegisterCommentService", registerCommentServiceProxy);
        logger.info("TransactionProxyPostProcessor done");
    }

    @Override
    public int order() {
        return 9;
    }
}
