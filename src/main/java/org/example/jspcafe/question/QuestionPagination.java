package org.example.jspcafe.question;


import org.example.jspcafe.common.Pagination;

import java.util.List;

public class QuestionPagination extends Pagination<Question> {
    public QuestionPagination(List<Question> items, int currentPage, int pageSize, long totalItems) {
        super(items, currentPage, pageSize, totalItems);
    }
}
