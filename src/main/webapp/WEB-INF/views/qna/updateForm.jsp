<%@ page import="com.woowa.cafe.dto.article.ArticleDto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<%@ include file="/WEB-INF/components/header.html" %>
<body>
<%@ include file="/WEB-INF/components/navbar-fixed-top-header.html" %>
<%@ include file="/WEB-INF/components/navbar-default.jsp" %>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default content-main">
            <% ArticleDto article = (ArticleDto) request.getAttribute("article"); %>
            <form id="articleForm">
                <div class="form-group">
                    <label for="title">제목</label>
                    <input type="text" class="form-control" id="title" name="title" value="<%=article.title()%>"
                           placeholder="제목"/>
                </div>
                <div class="form-group">
                    <label for="contents">내용</label>
                    <textarea name="contents" id="contents" rows="5" class="form-control">
<%=article.contents()%>
                    </textarea>
                </div>
                <button type="button" class="btn btn-success clearfix pull-right"
                        onclick="updateArticle()">수정하기
                </button>
                <div class="clearfix"></div>
            </form>
        </div>
    </div>
</div>

<script>
    var articleId = <%=article.articleId()%>;

    function updateArticle() {
        const form = document.getElementById('articleForm');
        const formData = new FormData(form);
        const urlParams = new URLSearchParams(formData);

        fetch(`/question/` + articleId, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: urlParams.toString(),
            redirect: 'manual'
        })
            .then(response => {
                if (response.type === 'opaqueredirect') {
                    window.location.href = response.headers.get('Location');
                } else if (response.ok) {
                    return response.text();
                } else {
                    throw new Error('Network response was not ok');
                }
            })
            .then(data => {
                console.log('Success:', data);
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    }
</script>

<!-- script references -->
<%@ include file="/WEB-INF/components/scirpt-refernces.html" %>
</body>
</html>
