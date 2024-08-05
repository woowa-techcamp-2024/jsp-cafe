<%@ page import="com.woowa.model.Question" %>
<%@ page import="java.time.ZonedDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="com.woowa.database.Page" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>SLiPP Java Web Programming</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>

<body>
<%@ include file="../header.jsp" %>
<%@ include file="../nav.jsp" %>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default qna-list">
            <ul class="list">
                <c:forEach var="question" items="${questions.content}" varStatus="status">
                    <%
                        Question question = (Question) pageContext.getAttribute("question");
                        String dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")
                                .format(question.getCreatedAt());
                        pageContext.setAttribute("createdAt", dateTime);
                    %>
                    <li>
                        <div class="wrap">
                            <div class="main">
                                <strong class="subject">
                                    <a href="/questions/${question.questionId}">${question.title}</a>
                                </strong>
                                <div class="auth-info">
                                    <i class="icon-add-comment"></i>
                                    <span class="time">${createdAt}</span>
                                    <a href="../user/profile.jsp" class="author">${question.author.nickname}</a>
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
                <div class="col-md-3"></div>
                <div class="col-md-6 text-center">
                    <%
                        Page<Question> questions = (Page<Question>) request.getAttribute("questions");
                        int prevSkipPage = Math.max(questions.getPage() - 5, 0);
                        int nextSkipPage = Math.min(questions.getPage() + 5, questions.getTotalPages() - 1);
                        int endPage = ((int) Math.ceil((double) (questions.getPage()+1) / 5) * 5);  // 1 ~ 5, 6 ~ 10
                        int startPage = Math.max(endPage - 4, 1);
                        endPage = Math.min(endPage, questions.getTotalPages());
                        pageContext.setAttribute("prevSkipPage", prevSkipPage);
                        pageContext.setAttribute("nextSkipPage", nextSkipPage);
                        pageContext.setAttribute("startPage", startPage);
                        pageContext.setAttribute("endPage", endPage);
                    %>
                    <ul class="pagination center-block" style="display:inline-block;">
                        <c:if test="${questions.page > 0}">
                            <li><a href="?page=${prevSkipPage}">«</a></li>
                        </c:if>
                        <c:forEach begin="${startPage}" end="${endPage}" var="i">
                            <c:choose>
                                <c:when test="${questions.page == i-1}">
                                    <li class="active"><span>${i}</span></li>
                                </c:when>
                                <c:otherwise>
                                    <li><a href="?page=${i-1}">${i}</a></li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${questions.page < questions.totalPages - 1}">
                            <li><a href="?page=${nextSkipPage}">»</a></li>
                        </c:if>
                    </ul>
                </div>
                <div class="col-md-3 qna-write">
                    <a href="${pageContext.request.contextPath}/qna/form.jsp" class="btn btn-primary pull-right"
                       role="button">질문하기</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/jquery-2.2.0.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/scripts.js"></script>
</body>
</html>