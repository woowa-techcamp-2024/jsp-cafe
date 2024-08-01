<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<!-- 게시판 -->
<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default qna-list">
            <ul class="list">
                <li>
                    <div class="summary">전체 글 ${page.totalElements}개</div>
                </li>
                <c:forEach var="post" items="${page.content}" varStatus="status">
                    <li>
                        <div class="wrap">
                            <div class="main">
                                <strong class="subject">
                                    <a href="posts/${post.postId}">${post.title}</a>
                                </strong>
                                <div class="auth-info">
                                    <i class="icon-add-comment"></i>
                                    <span class="time">${post.formattedDate}</span>
                                    <a href="/users/${post.authorId}" class="author">${post.authorUsername}</a>
                                </div>
                                <div class="reply" title="댓글">
                                    <i class="icon-reply"></i>
                                    <span class="point">${status.index + 1} </span>
                                </div>
                            </div>
                        </div>
                    </li>
                </c:forEach>
            </ul>
            <div class="row">
                <div class="col-md-3"></div>
                <div class="col-md-6 text-center">
                    <ul class="pagination center-block" style="display:inline-block;">
                        <c:if test="${!page.isFirstPage}">
                            <li><a href="/?pageNum=${page.pageNumber - 1}&pageSize=${page.pageSize}"><<</a></li>
                        </c:if>
                        <li>
                            <span>${page.pageNumber}</span>
                        </li>
                        <c:if test="${!page.isLastPage}">
                            <li><a href="/?pageNum=${page.pageNumber + 1}&pageSize=${page.pageSize}">>></a></li>
                        </c:if>
                    </ul>
                </div>
                <div class="col-md-3 qna-write">
                    <a href="/posts/create" class="btn btn-primary pull-right" role="button">글쓰기</a>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/include/footer.jsp" %>
