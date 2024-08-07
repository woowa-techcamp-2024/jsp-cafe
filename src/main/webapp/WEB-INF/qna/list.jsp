<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/functions.tld" prefix="fn" %>

<%@ include file="/WEB-INF/base/head.jsp" %>
<%@ include file="/WEB-INF/base/header.jsp" %>
<%@ include file="/WEB-INF/base/nav.jsp" %>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default qna-list">
            <ul class="list">
                <c:forEach var="question" items="${questions}">
                    <li>
                        <div class="wrap">
                            <div class="main">
                                <strong class="subject">
                                    <a href="/questions/${question.questionId}">${question.title}</a>
                                </strong>
                                <div class="auth-info">
                                    <i class="icon-add-comment"></i>
                                    <span class="time">${fn:formatDateTime(question.createdAt)}</span>
                                    <a href="/users" class="author">${question.writer}</a>
                                </div>
                                <div class="reply" title="댓글">
                                    <i class="icon-reply"></i>
                                    <span class="point">8</span>
                                </div>
                            </div>
                        </div>
                    </li>
                </c:forEach>
            </ul>
            <div class="row">
                <div class="col-md-12 text-center">
                    <ul class="pagination">
                        <c:if test="${currentPage > 1}">
                            <li><a href="?page=1">«</a></li>
                        </c:if>
                        <%
                            long currentPage = (long) request.getAttribute("currentPage");
                            long totalPage = (long) request.getAttribute("totalPage");

                            long startPage = currentPage - 2;
                            long endPage = currentPage + 2;

                            if (startPage < 1) {
                                endPage += (1 - startPage);
                                startPage = 1;
                            }
                            if (endPage > totalPage) {
                                startPage -= (endPage - totalPage);
                                endPage = totalPage;
                            }
                            if (startPage < 1) {
                                startPage = 1;
                            }

                            request.setAttribute("startPage", startPage);
                            request.setAttribute("endPage", endPage);
                        %>
                        <c:forEach begin="${startPage}" end="${endPage}" var="i">
                            <li class="<c:if test='${i == currentPage}'>active</c:if>">
                                <a href="?page=${i}">${i}</a>
                            </li>
                        </c:forEach>
                        <c:if test="${currentPage + 2< totalPage}">
                            <li><a href="?page=${totalPage}">»</a></li>
                        </c:if>
                    </ul>
                </div>
                <div class="col-md-3 qna-write">
                    <a href="/questions" class="btn btn-primary pull-right" role="button">질문하기</a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/base/footer.jsp" %>