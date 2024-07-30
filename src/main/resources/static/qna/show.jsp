<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="com.woowa.model.Question" %>
<%@ page import="com.woowa.model.Reply" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>SLiPP Java Web Programming</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <link href="../css/styles.css" rel="stylesheet">
</head>
<body>
<%@ include file="../header.jsp" %>
<%@ include file="../nav.jsp" %>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
            <%
                Question question = (Question) request.getAttribute("question");
                String dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm").format(question.getCreatedAt());
                pageContext.setAttribute("createdAt", dateTime);
            %>
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
                          <a href="/users/92/kimmunsu" class="article-author-name">${question.author.nickname}</a>
                          <a href="/questions/413" class="article-header-time" title="퍼머링크">
                              ${createdAt}
                              <i class="icon-link"></i>
                          </a>
                      </div>
                  </div>
                  <div class="article-doc">
                      ${question.content}
                  </div>
                  <div class="article-util">
                      <ul class="article-util-list">
                          <li>
                              <a class="link-modify-article" href="/questions/${question.questionId}/update">수정</a>
                          </li>
                          <li>
                              <form class="form-delete" action="/questions/${question.questionId}" method="POST">
                                  <input type="hidden" name="_method" value="DELETE">
                                  <button class="link-delete-article" type="submit">삭제</button>
                              </form>
                          </li>
                          <li>
                              <a class="link-modify-article" href="<c:url value="/"/>">목록</a>
                          </li>
                      </ul>
                  </div>
              </article>

              <div class="qna-comment">
                  <div class="qna-comment-slipp">
                      <p class="qna-comment-count"><strong>${fn:length(question.replies)}</strong>개의 의견</p>
                      <div class="qna-comment-slipp-articles">
                          <c:forEach var="reply" items="${question.replies}" varStatus="status">
                          <%
                              Reply reply = (Reply) pageContext.getAttribute("reply");
                              String replyDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm").format(reply.getCreatedAt());
                              pageContext.setAttribute("createdAt", replyDateTime);
                          %>
                          <article class="article" id="answer-1405">
                              <div class="article-header">
                                  <div class="article-header-thumb">
                                      <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">
                                  </div>
                                  <div class="article-header-text">
                                      <a href="/users/${reply.author.userId}" class="article-author-name">${reply.author.nickname}</a>
                                      <a href="#answer-1434" class="article-header-time" title="퍼머링크">
                                          ${replyDateTime}
                                      </a>
                                  </div>
                              </div>
                              <div class="article-doc comment-doc">
                                  <p>${reply.content}</p>
                              </div>
                              <div class="article-util">
                                  <ul class="article-util-list">
                                      <li>
                                          <a class="link-modify-article" href="/questions/413/answers/1405/form">수정</a>
                                      </li>
                                      <li>
                                          <form class="delete-answer-form" action="/questions/413/answers/1405" method="POST">
                                              <input type="hidden" name="_method" value="DELETE">
                                              <button type="submit" class="delete-answer-button">삭제</button>
                                          </form>
                                      </li>
                                  </ul>
                              </div>
                          </article>
                          </c:forEach>
                          <form class="submit-write" action="/questions/${question.questionId}/replies" method="post">
                              <div class="form-group" style="padding:14px;">
                                  <textarea class="form-control" name="content" placeholder="Update your status"></textarea>
                              </div>
                              <button class="btn btn-success pull-right" type="submit">답변하기</button>
                              <div class="clearfix" />
                          </form>
                      </div>
                  </div>
              </div>
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

<!-- script references -->
<script src="../js/jquery-2.2.0.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/scripts.js"></script>
	</body>
</html>