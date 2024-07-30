function createReply(questionId) {
    const content = $('#reply-content').val();

    postJsonAPI(`/replies`, {content, questionId}, `/questions/${questionId}`);
}