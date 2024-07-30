<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/include/header.jsp" %>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default content-main">
            <form name="question" method="post" action="/posts/create">
                <input type="hidden" name="authorId" value="${sessionScope.userPrincipal.id}"/>
                <div class="form-group">
                    <label for="author_id">글쓴이</label>
                    <input type="text" class="form-control" id="author_id"
                           value="${sessionScope.userPrincipal.username}" placeholder="글쓴이" disabled/>
                </div>
                <div class="form-group">
                    <label for="title">제목</label>
                    <input type="text" class="form-control" id="title" name="title" placeholder="제목" required/>
                </div>
                <div class="form-group">
                    <label for="content">내용</label>
                    <textarea name="content" id="content" rows="5" class="form-control" required></textarea>
                </div>
                <button type="submit" class="btn btn-success clearfix pull-right">질문하기</button>
                <div class="clearfix"/>
            </form>
        </div>
    </div>
</div>
</div>
<%@ include file="/WEB-INF/views/include/footer.jsp" %>
