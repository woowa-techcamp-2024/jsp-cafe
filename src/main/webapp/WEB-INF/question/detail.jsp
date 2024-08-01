<%@ page contentType="text/html; charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <title>질문 상세 페이지</title>
    <%@include file="/WEB-INF/includes/head.jsp"%>
</head>
<body>

<%@include file="/WEB-INF/includes/navbar-fixed-top.jsp"%>
<%@include file="/WEB-INF/includes/navbar-default.jsp"%>

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
                            <img src="https://graph.facebook.com/v2.3/100000059371774/picture" class="article-author-thumb" alt="">
                        </div>
                        <div class="article-header-text">
                            <a href="/userPage?action=detail&seq=${question.userSeq}" class="article-author-name">${question.writer}</a>
                            <a href="/questionPage?action=detail&seq=${question.questionSeq}" class="article-header-time" title="퍼머링크">
                                2015-12-30 01:47
                                <i class="icon-link"></i>
                            </a>
                        </div>
                    </div>
                    <div class="article-doc">
                        <p>${question.contents}</p>
                    </div>
                    <div class="article-util">
                        <ul class="article-util-list">
                            <c:if test="${question.userSeq == sessionScope.userSeq}">
                            <li>
                                <a class="link-modify-article" href="/questionPage?action=update&seq=${question.questionSeq}">수정</a>
                            </li>
                            <li>
                                <form class="form-delete" id="form-delete">
                                    <input type="hidden" name="_method" value="DELETE">
                                    <button class="link-delete-article" type="button" onclick="deleteQuestion()">삭제</button>
                                </form>
                            </li>
                            </c:if>
                            <li>
                                <a class="link-modify-article" href="/">목록</a>
                            </li>
                        </ul>
                    </div>
                </article>

<%--                <div class="qna-comment">--%>
<%--                    <div class="qna-comment-slipp">--%>
<%--                        <p class="qna-comment-count"><strong>2</strong>개의 의견</p>--%>
<%--                        <div class="qna-comment-slipp-articles">--%>

<%--                            <article class="article" id="answer-1405">--%>
<%--                                <div class="article-header">--%>
<%--                                    <div class="article-header-thumb">--%>
<%--                                        <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">--%>
<%--                                    </div>--%>
<%--                                    <div class="article-header-text">--%>
<%--                                        <a href="/users/1/자바지기" class="article-author-name">자바지기</a>--%>
<%--                                        <a href="#answer-1434" class="article-header-time" title="퍼머링크">--%>
<%--                                            2016-01-12 14:06--%>
<%--                                        </a>--%>
<%--                                    </div>--%>
<%--                                </div>--%>
<%--                                <div class="article-doc comment-doc">--%>
<%--                                    <p>이 글만으로는 원인 파악하기 힘들겠다. 소스 코드와 설정을 단순화해서 공유해 주면 같이 디버깅해줄 수도 있겠다.</p>--%>
<%--                                </div>--%>
<%--                                <div class="article-util">--%>
<%--                                    <ul class="article-util-list">--%>
<%--                                        <li>--%>
<%--                                            <a class="link-modify-article" href="/questions/413/answers/1405/form">수정</a>--%>
<%--                                        </li>--%>
<%--                                        <li>--%>
<%--                                            <form class="delete-answer-form" action="/questions/413/answers/1405" method="POST">--%>
<%--                                                <input type="hidden" name="_method" value="DELETE">--%>
<%--                                                <button type="submit" class="delete-answer-button">삭제</button>--%>
<%--                                            </form>--%>
<%--                                        </li>--%>
<%--                                    </ul>--%>
<%--                                </div>--%>
<%--                            </article>--%>
<%--                            <article class="article" id="answer-1406">--%>
<%--                                <div class="article-header">--%>
<%--                                    <div class="article-header-thumb">--%>
<%--                                        <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">--%>
<%--                                    </div>--%>
<%--                                    <div class="article-header-text">--%>
<%--                                        <a href="/users/1/자바지기" class="article-author-name">자바지기</a>--%>
<%--                                        <a href="#answer-1434" class="article-header-time" title="퍼머링크">--%>
<%--                                            2016-01-12 14:06--%>
<%--                                        </a>--%>
<%--                                    </div>--%>
<%--                                </div>--%>
<%--                                <div class="article-doc comment-doc">--%>
<%--                                    <p>이 글만으로는 원인 파악하기 힘들겠다. 소스 코드와 설정을 단순화해서 공유해 주면 같이 디버깅해줄 수도 있겠다.</p>--%>
<%--                                </div>--%>
<%--                                <div class="article-util">--%>
<%--                                    <ul class="article-util-list">--%>
<%--                                        <li>--%>
<%--                                            <a class="link-modify-article" href="/questions/413/answers/1405/form">수정</a>--%>
<%--                                        </li>--%>
<%--                                        <li>--%>
<%--                                            <form class="form-delete" action="/questions/413/answers/1405" method="POST">--%>
<%--                                                <input type="hidden" name="_method" value="DELETE">--%>
<%--                                                <button type="submit" class="delete-answer-button">삭제</button>--%>
<%--                                            </form>--%>
<%--                                        </li>--%>
<%--                                    </ul>--%>
<%--                                </div>--%>
<%--                            </article>--%>
<%--                            <form class="submit-write">--%>
<%--                                <div class="form-group" style="padding:14px;">--%>
<%--                                    <textarea class="form-control" placeholder="Update your status"></textarea>--%>
<%--                                </div>--%>
<%--                                <button class="btn btn-success pull-right" type="button">답변하기</button>--%>
<%--                                <div class="clearfix" />--%>
<%--                            </form>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </div>--%>
            </div>
        </div>
    </div>
</div>

<script type="text/template" id="answerTemplate">
    <article class="article">
        <div class="article-header">
            <div class="article-header-thumb">
                <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">
            </div>
            <div class="article-header-text">
                <a href="#" class="article-author-name">{0}</a>
                <div class="article-header-time">{1}</div>
            </div>
        </div>
        <div class="article-doc comment-doc">
            {2}
        </div>
        <div class="article-util">
            <ul class="article-util-list">
                <li>
                    <a class="link-modify-article" href="/api/qna/updateAnswer/{3}">수정</a>
                </li>
                <li>
                    <form class="delete-answer-form" action="/api/questions/{3}/answers/{4}" method="POST">
                        <input type="hidden" name="_method" value="DELETE">
                        <button type="submit" class="delete-answer-button">삭제</button>
                    </form>
                </li>
            </ul>
        </div>
    </article>
</script>

<script>
function deleteQuestion() {
    const jsonData = {
        "seq": "${question.questionSeq}"
    };

    fetch(`/questions`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonData)
    }).then(response => {
        if (response.ok) {
            window.location.href = "/"; // 질문 목록 페이지로 리디렉트
        } else {
            // 오류 처리
            alert("질문 삭제 중 오류가 발생했습니다.");
        }
    }).catch(error => {
        console.error("Error:", error);
        alert("질문 삭제 중 오류가 발생했습니다.");
    });
}
</script>
<!-- script references -->
<%@include file="/WEB-INF/includes/script-references.jsp"%>
</body>
</html>