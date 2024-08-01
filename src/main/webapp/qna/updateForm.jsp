<%@ page import="domain.Article" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="kr">
<%@ include file="../header.jsp" %>
<body>
<%@ include file="../navigationbar.jsp" %>
<% Article article = (Article) request.getAttribute("article"); %>
<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default content-main">
            <form name="question" id="questionForm">
                <div class="form-group">
                    <label for="title">제목</label>
                    <input type="text" class="form-control" id="title" name="title" placeholder="제목" value="<%= article.getTitle() %>"/>
                </div>
                <div class="form-group">
                    <label for="content">내용</label>
                    <textarea name="content" id="content" rows="5" class="form-control"><%= article.getContent() %> </textarea>
                </div>
                <button type="button" class="btn btn-success clearfix pull-right" id="updateButton">수정하기</button>
                <div class="clearfix" />
            </form>
        </div>
    </div>
</div>
<%@ include file="../footer.jsp" %>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function() {
        $('#updateButton').click(function() {
            var title = $('#title').val();
            var content = $('#content').val();

            $.ajax({
                url: '/questions/<%= article.getId() %>',
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify({
                    title: title,
                    content: content
                }),
                success: function(response) {
                    alert('질문이 수정되었습니다.');
                    window.location.href = '/';
                },
                error: function(xhr, status, error) {
                    console.log("error: " + error);
                    if(xhr.status === 401) {
                        alert('다른 사용자의 게시글을 수정할 수 없습니다.');
                    }
                }
            });
        });
    });
</script>
</body>
</html>