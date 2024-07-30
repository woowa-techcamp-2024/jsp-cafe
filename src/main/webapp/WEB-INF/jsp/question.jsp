<%--
  Created by IntelliJ IDEA.
  User: KyungMin Lee
  Date: 24. 7. 23.
  Time: 오후 01:00
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE>
<html lang="ko">
<jsp:include page="/WEB-INF/jsp/component/headers.jsp"/>
<body>
<jsp:include page="/WEB-INF/jsp/component/navbar.jsp"/>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
            <c:set var="articleCommonResponse" value="${requestScope.article}"/>
            <header class="qna-header">
                <h2 class="qna-title">${articleCommonResponse.title}</h2>
            </header>
            <div class="content-main">
                <article class="article">
                    <div class="article-header">
                        <div class="article-header-thumb">
                            <img src="<c:url value="/resources/images/80-text.png"/>"
                                 class="article-author-thumb" alt="">
                        </div>
                        <div class="article-header-text">
                            <a href="/users/${articleCommonResponse.writerUserId}"
                               class="article-author-name"><c:out
                                    value="${articleCommonResponse.writerUsername}"/>
                            </a>
                            <a href="/questions/${articleCommonResponse.id}"
                               class="article-header-time" title="퍼머링크">
                                ${articleCommonResponse.createdAt}
                                <i class="icon-link"></i>
                            </a>
                        </div>
                    </div>
                    <div class="article-doc">
                        <c:out value="${articleCommonResponse.contents}"/>
                    </div>
                    <div class="article-util">
                        <ul class="article-util-list">
                            <li>
                                <a class="link-modify-article"
                                   href="/questions/${articleCommonResponse.id}/form">수정</a>
                            </li>
                            <li>
                                <form id="questionDelete" class="form-delete"
                                      action="/questions/${articleCommonResponse.id}" method="POST">
                                    <button class="link-delete-article" type="submit">삭제</button>
                                </form>
                            </li>
                            <li>
                                <a class="link-modify-article"
                                   href="${pageContext.request.contextPath}/index.html">목록</a>
                            </li>
                        </ul>
                    </div>
                </article>
                <c:set var="replies" value="${requestScope.replies}"/>
                <div class="qna-comment">
                    <div class="qna-comment-slipp">
                        <p class="qna-comment-count"><strong>${requestScope.repliesLength}</strong>개의
                            의견</p>
                        <div class="qna-comment-slipp-articles">
                            <c:forEach var="reply" items="${replies}">
                                <article class="article" id="answer-1405">
                                    <div class="article-header">
                                        <div class="article-header-thumb">
                                            <img src="<c:url value="/resources/images/80-text.png"/>"
                                                 class="article-author-thumb" alt="">
                                        </div>
                                        <div class="article-header-text">
                                            <a href="/users/${reply.userId}"
                                               class="article-author-name">${reply.username}</a>
                                            <a class="article-header-time"
                                               title="퍼머링크">${reply.createdAt}</a>
                                        </div>
                                    </div>
                                    <div class="article-doc comment-doc">
                                        <p>${reply.contents}</p>
                                    </div>
                                    <div class="article-util">
                                        <ul class="article-util-list">
                                            <li>
                                                <a class="link-modify-article"
                                                   href="/replies/${reply.id}">수정</a>
                                            </li>
                                            <li>
                                                <form class="delete-answer-form" id="replyDelete"
                                                      name="replyDelete"
                                                      action="/replies/${reply.id}" method="POST">
                                                    <button type="submit"
                                                            class="delete-answer-button">
                                                        삭제
                                                    </button>
                                                </form>
                                            </li>
                                        </ul>
                                    </div>
                                </article>
                            </c:forEach>


                            <form class="submit-write" method="POST"
                                  action="<c:url value="/replies"/>">
                                <input type="hidden" id="article" name="article"
                                       value=${articleCommonResponse.id}>
                                <div class="form-group" style="padding:14px;">
                                    <textarea id="contents" name="contents" class="form-control"
                                              placeholder="Update your status"></textarea>
                                </div>
                                <button class="btn btn-success pull-right" type="submit">답변하기
                                </button>
                                <div class="clearfix"></div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%--<script type="text/template" id="answerTemplate">--%>
<%--    <article class="article">--%>
<%--        <div class="article-header">--%>
<%--            <div class="article-header-thumb">--%>
<%--                <img src="https://graph.facebook.com/v2.3/1324855987/picture"--%>
<%--                     class="article-author-thumb"--%>
<%--                     alt="">--%>
<%--            </div>--%>
<%--            <div class="article-header-text">--%>
<%--                <a href="#" class="article-author-name">{0}</a>--%>
<%--                <div class="article-header-time">{1}</div>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        <div class="article-doc comment-doc">--%>
<%--            {2}--%>
<%--        </div>--%>
<%--        <div class="article-util">--%>
<%--            <ul class="article-util-list">--%>
<%--                <li>--%>
<%--                    <a class="link-modify-article" href="/api/qna/updateAnswer/{3}">수정</a>--%>
<%--                </li>--%>
<%--                <li>--%>
<%--                    <form class="delete-answer-form" action="/api/questions/{3}/answers/{4}"--%>
<%--                          method="POST">--%>
<%--                        <input type="hidden" name="_method" value="DELETE">--%>
<%--                        <button type="submit" class="delete-answer-button">삭제</button>--%>
<%--                    </form>--%>
<%--                </li>--%>
<%--            </ul>--%>
<%--        </div>--%>
<%--    </article>--%>
<%--</script>--%>

<jsp:include page="/WEB-INF/jsp/component/scripts.jsp"/>
</body>
</html>

<script>
  $(document).ready(function () {
    $('#questionDelete').on('submit', function (e) {
      e.preventDefault();

      let form = $(this);
      let actionUrl = form.attr('action');

      $.ajax({
        url: actionUrl,
        type: 'DELETE',
        success: function () {
          window.location.href = '/index.html';
        },
        error: function (xhr, status, error) {
          let errorMessage = xhr.responseText;
          if (confirm(status + ": " + errorMessage)) {
            window.location.href = '/questions/' + ${articleCommonResponse.id};
          }
        }
      });
    })
  })
</script>
<script>
  $(document).ready(function () {
    $('#replyDelete').on('submit', function (e) {
      e.preventDefault();

      let form = $(this);
      let actionUrl = form.attr('action');

      $.ajax({
        url: actionUrl,
        type: 'DELETE',
        success: function () {
          window.location.href = '/questions/' + ${articleCommonResponse.id};
        },
        error: function (xhr, status, error) {
          let errorMessage = xhr.responseText;
          if (confirm(status + ": " + errorMessage)) {
            window.location.href = '/questions/' + ${articleCommonResponse.id};
          }
        }
      });
    });
  })
</script>