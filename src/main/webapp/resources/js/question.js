function deleteQuestion(questionId) {
    deleteAPI(`/questions/${questionId}`, '/');
}
