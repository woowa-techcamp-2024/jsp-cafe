function deleteQuestion(questionId) {
    deleteAPI(`/questions/${questionId}`, (response) => window.location.href = '/');
}

function writeQuestion() {
    const title = $('#title').val();
    const contents = $('#contents').val();

    postAPI(`/questions`, {title, contents}, '/');
}

function updateQuestion(questionId) {
    const title = $('#title').val();
    const contents = $('#contents').val();

    putAPI(`/questions/${questionId}`, {questionId, title, contents}, `/questions/${questionId}`);
}

function clickSubmit(questionId) {
    if (questionId) {
        updateQuestion(questionId);
    } else {
        writeQuestion();
    }
}