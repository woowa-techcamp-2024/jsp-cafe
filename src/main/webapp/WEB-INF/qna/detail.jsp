<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/functions.tld" prefix="fn" %>
<%@ include file="/WEB-INF/base/head.jsp" %>
<%@ include file="/WEB-INF/base/header.jsp" %>
<%@ include file="/WEB-INF/base/nav.jsp" %>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
            <header class="qna-header">
                <h2 class="qna-title">${question.title}</h2>
            </header>
            <div class="content-main">
                <article class="article">
                    <div class="article-header">
                        <div class="article-header-thumb">
                            <img src="https://graph.facebook.com/v2.3/100000059371774/picture"
                                 class="article-author-thumb" alt="">
                        </div>
                        <div class="article-header-text">
                            <a href="/users/92/kimmunsu" class="article-author-name">${question.writer}</a>
                            <a href="/questions/413" class="article-header-time" title="퍼머링크">
                                ${fn:formatDateTime(question.createdAt)}
                                <i class="icon-link"></i>
                            </a>
                        </div>
                    </div>
                    <div class="article-doc">
                        <p>
                            ${question.content}
                        </p>
                    </div>

                    <div class="article-util">
                        <ul class="article-util-list">
                            <c:if test="${sessionScope.userId == question.writer}">
                                <li>
                                    <a class="link-modify-article"
                                       href="/questions/${question.questionId}?edit=true">수정</a>
                                </li>
                                <li>
                                    <button id="delete-question-btn" class="delete-answer-button"
                                            onclick="deleteQuestion(${question.questionId})">삭제
                                    </button>
                                </li>
                            </c:if>
                            <li>
                                <a class="link-modify-article" href="/">목록</a>
                            </li>
                        </ul>
                    </div>
                </article>

                <div class="qna-comment">
                    <div class="qna-comment-slipp">
                        <p class="qna-comment-count"><strong>2</strong>개의 의견</p>
                        <div class="qna-comment-slipp-articles">
                            <c:forEach var="reply" items="${replies}">
                                <article class="article" id="answer-1405">
                                    <div class="article-header">
                                        <div class="article-header-thumb">
                                            <img src="https://graph.facebook.com/v2.3/1324855987/picture"
                                                 class="article-author-thumb" alt="">
                                        </div>
                                        <div class="article-header-text">
                                            <a href="/users/${reply.writer}"
                                               class="article-author-name">${reply.writer}</a>
                                            <a href="#answer-1434" class="article-header-time" title="퍼머링크">
                                                    ${fn:formatDateTime(reply.createdAt)}
                                            </a>
                                        </div>
                                    </div>
                                    <div class="article-doc comment-doc">
                                        <p>${reply.content}</p>
                                    </div>
                                    <div class="article-util">
                                        <ul class="article-util-list">
                                            <c:if test="${sessionScope.userId == reply.writer}">
                                                <li>
                                                    <button id="delete-reply-btn" class="delete-answer-button"
                                                            onclick="deleteReply(${reply.replyId}, ${question.questionId})">
                                                        삭제
                                                    </button>
                                                </li>
                                            </c:if>
                                        </ul>
                                    </div>
                                </article>
                            </c:forEach>
                            <form class="submit-write">
                                <div class="form-group" style="padding:14px;">
                                    <textarea class="form-control" placeholder="Update your status"
                                              id="reply-content"></textarea>
                                </div>
                                <button class="btn btn-success pull-right" type="button"
                                        onclick="createReply(${question.questionId})">답변하기
                                </button>
                                <div class="clearfix"/>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="/resources/js/ApiTemplate.js"></script>
<script src="/resources/js/question.js"></script>
<script src="/resources/js/reply.js"></script>
<%@ include file="/WEB-INF/base/footer.jsp" %>