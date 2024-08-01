package codesquad.common.db.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TxProxy implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(TxProxy.class);

    private final Object target;
    private final TxManager txManager;

    public TxProxy(Object target, TxManager txManager) {
        this.target = target;
        this.txManager = txManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        txManager.begin();
        logger.info("transaction started");
        try {
            Object result = method.invoke(target, args);
            txManager.commit();
            logger.info("transaction committed");
            return result;
        } catch (Exception e) {
            txManager.rollback();
            logger.info("transaction rollback", e);
            throw e;
        }
    }
}
