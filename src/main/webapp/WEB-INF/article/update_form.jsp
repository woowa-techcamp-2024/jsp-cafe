<%@ page import="cafe.domain.entity.Article" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/components/head.jsp" %>
<body>
<%@ include file="/WEB-INF/components/header.jsp"%>
<%@ include file="/WEB-INF/components/navigation.jsp"%>

<% Article article = (Article) request.getAttribute("article"); %>
<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default content-main">
            <form id="article-form" name="article" method="post" action="/articles/<%= article.getArticleId() %>">
                <div class="form-group">
                    <label for="title">제목</label>
                    <input type="text" class="form-control" id="title" name="title" value="<%= article.getTitle() %>"/>
                </div>
                <div class="form-group">
                    <label for="contents">내용</label>
                    <textarea name="contents" id="contents" rows="5" class="form-control"><%= article.getContents() %></textarea>
                </div>
                <button type="submit" class="btn btn-success clearfix pull-right">수정하기</button>
            </form>
        </div>
    </div>
</div>

<script>
    document.getElementById('article-form').addEventListener('submit', function(event) {
        event.preventDefault();

        const articleId = '<%= article.getArticleId() %>';
        const formData = new FormData(this);

        const xhr = new XMLHttpRequest();
        xhr.open('PUT', "/articles/" + articleId + "/update", true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.onload = function() {
            if (xhr.status === 200) {
                alert('수정이 완료되었습니다.');
                window.location.href = '/articles/' + articleId;
            } else {
                alert('수정이 실패했습니다.');
            }
        };
        xhr.onerror = function() {
            console.error('Error:', xhr.statusText);
            alert('An error occurred. Please try again.');
        };

        const jsonData = JSON.stringify({
            title: formData.get('title'),
            contents: formData.get('contents')
        });

        xhr.send(jsonData);
    });
</script>

<!-- script references -->
<%@ include file="/WEB-INF/components/script.jsp" %>
</body>
</html>