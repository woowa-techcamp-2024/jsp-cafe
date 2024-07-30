package com.woowa.database;

import com.woowa.model.Reply;
import java.util.List;

public interface ReplyDatabase {
    void save(Reply reply);

    List<Reply> findAll();
}
