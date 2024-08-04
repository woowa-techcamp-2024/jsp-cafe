package codesquad.common.db.transaction;

import codesquad.common.db.connection.ConnectionManager;

public interface TxManager extends ConnectionManager {
    void begin();

    void commit();

    void rollback();
}
