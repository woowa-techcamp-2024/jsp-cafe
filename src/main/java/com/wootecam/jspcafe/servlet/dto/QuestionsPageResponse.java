package com.wootecam.jspcafe.servlet.dto;

import com.wootecam.jspcafe.domain.Question;
import java.util.List;

public class QuestionsPageResponse {

    private final int questionCount;
    private final int currentPage;
    private final List<QuestionResponse> questionResponses;

    public QuestionsPageResponse(final int questionCount, final int currentPage, final List<QuestionResponse> questionResponses) {
        this.questionCount = questionCount;
        this.currentPage = currentPage;
        this.questionResponses = questionResponses;
    }

    public static QuestionsPageResponse of(final int questionCount, final int currentPage, final List<Question> questions) {
        List<QuestionResponse> questionResponses = questions.stream()
                .map(QuestionResponse::from)
                .toList();

        return new QuestionsPageResponse(
                questionCount,
                currentPage,
                questionResponses
        );
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<QuestionResponse> getQuestionResponses() {
        return questionResponses;
    }
}
