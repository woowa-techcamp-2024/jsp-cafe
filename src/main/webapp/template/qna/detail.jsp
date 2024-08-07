<%@ page import="woowa.cafe.dto.QuestionInfo" %>
<%@ page import="woowa.cafe.dto.ReplyInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="woowa.frame.web.collection.Page" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>SLiPP Java Web Programming</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <link href="/static/css/styles.css" rel="stylesheet">
</head>
<body>
<%@ include file="/template/common/header.jsp" %>

<% QuestionInfo question = (QuestionInfo) request.getAttribute("question"); %>
<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
            <header class="qna-header">
                <h2 class="qna-title"><%=question.title()%>
                </h2>
            </header>
            <div class="content-main">
                <article class="article">
                    <div class="article-header">
                        <div class="article-header-thumb">
                            <img src="https://graph.facebook.com/v2.3/100000059371774/picture"
                                 class="article-author-thumb" alt="">
                        </div>
                        <div class="article-header-text">
                            <a href="" class="article-author-name"><%=question.authorName()%>
                            </a>
                            <a href="" class="article-header-time" title="퍼머링크">
                                <%=question.createdAt().toLocalDate().toString()%>
                                <i class="icon-link"></i>
                            </a>
                        </div>
                    </div>

                    <div class="article-doc">

                        <% for (String content : question.content().split("\n")) { %>
                        <p><%=content%>
                        </p>
                        <% } %>
                    </div>
                    <div class="article-util">
                        <ul class="article-util-list">
                            <li>
                                <a class="link-modify-article" href="/question/<%=question.id()%>/form">수정</a>
                            </li>
                            <li>
                                <a class="link-modify-article" onclick="delete_question()">삭제</a>
                            </li>
                            <script>
                                function delete_question() {
                                    const url = "/question/<%=question.id()%>";
                                    fetch(url, {
                                        method: 'DELETE'
                                    }).then(response => {
                                        if (response.ok) {
                                            window.location.href = "/";
                                        } else {
                                            alert('삭제에 실패했습니다.');
                                        }
                                    });
                                }
                            </script>
                            <li>
                                <a class="link-modify-article" href="/">목록</a>
                            </li>
                        </ul>
                    </div>
                </article>

                <%
                    Page<ReplyInfo> pages = (Page<ReplyInfo>) request.getAttribute("replies");
                    List<ReplyInfo> replies = pages.getContent();
                %>
                <div class="qna-comment">
                    <div class="qna-comment-slipp">
                        <p class="qna-comment-count"><strong id="reply-count"><%=pages.getTotalElements()%>
                        </strong>개의 의견</p>
                        <div class="qna-comment-slipp-articles">
                            <div id="reply-box">
                                <% for (ReplyInfo reply : replies) { %>
                                <article id="reply-<%=reply.id()%>" class="article">
                                    <div class="article-header">
                                        <div class="article-header-thumb">
                                            <img src="https://graph.facebook.com/v2.3/1324855987/picture"
                                                 class="article-author-thumb" alt="">
                                        </div>
                                        <div class="article-header-text">
                                            <a href="#" class="article-author-name"><%=reply.authorName()%>
                                            </a>
                                            <a href="#" class="article-header-time" title="퍼머링크">
                                                <%=reply.createdDate().toLocalDate().toString()%>
                                            </a>
                                        </div>
                                    </div>
                                    <div class="article-doc comment-doc">
                                        <% for (String s : reply.content().split("\n")) {%>
                                        <p><%=s%>
                                        </p>
                                        <%}%>
                                    </div>
                                    <div class="article-util">
                                        <ul class="article-util-list">
                                            <li>
                                                <a class="link-modify-article"
                                                   href="/question/<%=question.id()%>/reply/<%=reply.id()%>">수정</a>
                                            </li>
                                            <li>
                                                <input type="hidden" name="_method" value="DELETE">
                                                <button type="button" class="delete-answer-button"
                                                        onclick="{
                                                                incrementReplyCount(-1);
                                                                deleteReply('<%=reply.questionId()%>','<%=reply.id()%>');
                                                                }">삭제
                                                </button>
                                            </li>
                                        </ul>
                                    </div>
                                </article>
                                <%}%>
                            </div>
                            <div>
                                <button class="btn btn-success pull-right" type="button" onclick="getMoreReply(<%=question.id()%>)">더보기</button>
                            </div>
                            <form class="submit-write">
                                <div class="form-group" style="padding:14px;">
                                    <textarea id="replyContent" class="form-control"
                                              placeholder="Update your status"></textarea>
                                </div>
                                <button class="btn btn-success pull-right" type="button" onclick="createReply('<%=question.id()%>')">답변하기
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
<script src="/static/js/jquery-2.2.0.min.js"></script>
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/scripts.js"></script>
<script src="/static/js/reply.js"></script>
</body>
</html>
