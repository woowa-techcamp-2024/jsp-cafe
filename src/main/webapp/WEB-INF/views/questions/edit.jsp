<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-8">
        <h2 class="mt-5">Edit Post</h2>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
        </c:if>
        <form id="editForm">
            <div class="mb-3">
                <label for="title" class="form-label">Title</label>
                <input type="text" class="form-control" id="title" name="title"
                       value="<c:out value="${article.title}" />" required>
            </div>
            <div class="mb-3">
                <label for="content" class="form-label">Content</label>
                <textarea class="form-control" id="content" name="content" rows="5"
                          required></textarea>
            </div>
            <button type="button" class="btn btn-primary" onclick="updateArticle()">Save Changes</button>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/views/footer.jsp" %>

<!-- Add axios.min.js -->
<script src="${pageContext.request.contextPath}/static/js/axios.min.js"></script>
<script>
    // 서버 측 변수를 전역 객체로 설정
    window.serverData = {
        content: `<c:out value="${article.content}"/>`,
    }
    $(document).ready(function () {
        $('#content').html(DOMPurify.sanitize(serverData.content));
    });

    async function updateArticle() {
        const title = document.getElementById('title').value;
        const content = document.getElementById('content').value;
        const articleId = '<c:out value="${article.id}" />';

        try {
            const response = await axios.put('${pageContext.request.contextPath}/questions/' + articleId, {
                title: title,
                content: content
            });

            if (response.status === 200) {
                window.location.href = '${pageContext.request.contextPath}/questions/' + articleId;
            } else {
                alert('Error: ' + response.status);
            }
        } catch (error) {
            if (error.response) {
                // 서버가 응답한 경우 (2xx 범위 외)
                alert('Error: ' + error.response.data);
            } else if (error.request) {
                // 요청이 이루어졌으나 응답을 받지 못한 경우
                alert('잠시 후 다시 실행해 주세요');
            }
        }
    }
</script>
