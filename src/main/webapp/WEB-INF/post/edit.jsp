<%@ page import="org.example.demo.domain.Post" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="kr" xmlns:jsp="http://java.sun.com/JSP/Page">

<jsp:include page="/components/header.jsp"/>
<body>
<jsp:include page="/components/nav.jsp"/>
<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <%
            Post post = (Post) request.getAttribute("post");
        %>
        <div class="panel panel-default">
            <header class="qna-header">
                <h2 class="qna-title">글 수정</h2>
            </header>
            <div class="content-main">
                <form action="/posts/<%=post.getId()%>" method="POST">
                    <input type="hidden" name="_method" value="PUT">
                    <div class="form-group">
                        <label for="title">제목</label>
                        <input type="text" class="form-control" id="title" name="title" value="<c:out value="${post.title}" />">
                    </div>
                    <div class="form-group">
                        <label for="contents">내용</label>
                        <textarea class="form-control" id="contents" name="contents"
                                  rows="5"><c:out value="${post.contents}" /></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">저장</button>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/components/footer.jsp"/>
</body>
</html>
