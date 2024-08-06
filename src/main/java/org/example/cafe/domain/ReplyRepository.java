package org.example.cafe.domain;

import java.util.List;
import org.example.cafe.application.ReplyService.ReplyPageDto;
import org.example.cafe.common.page.Page;

public interface ReplyRepository {

    Long save(Reply reply);

    Reply findById(Long id);

    List<Reply> findByQuestionId(Long questionId);

    Page<Reply> findByQuestionId(ReplyPageDto replyPageDto);

    void update(Reply reply);

    void deleteByQuestionId(Long questionId);

    void deleteAll();
}
