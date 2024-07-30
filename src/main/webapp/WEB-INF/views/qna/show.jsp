<%@ page import="com.woowa.cafe.dto.article.ArticleDto" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="com.woowa.cafe.dto.article.ReplyDto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<%@ include file="/WEB-INF/components/header.html" %>
<body>
<%@ include file="/WEB-INF/components/navbar-fixed-top-header.html" %>
<%@ include file="/WEB-INF/components/navbar-default.jsp" %>
<div class="container" id="main">
    <% ArticleDto article = (ArticleDto) request.getAttribute("article");%>
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
            <header class="qna-header">
                <h2 class="qna-title"><%= article.title()%>
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
                            <a href="<%= "/user/" + article.writerId()%>>"
                               class="article-author-name"><%= article.writerName()%>
                                ></a>
                            <a href="/questions/413" class="article-header-time" title="퍼머링크">
                                <%= article.updatedAt()%>
                                <i class="icon-link"></i>
                            </a>
                        </div>
                    </div>
                    <div class="article-doc" style="white-space: pre-wrap;">
                        <%= article.contents().trim() %>
                    </div>
                    <div class="article-util">
                        <ul class="article-util-list">
                            <li>
                                <a class="link-modify-article" href="<%="/question/" + article.articleId() +"/form"%>">수정</a>
                            </li>
                            <li>
                                <button type="button" class="link-modify-article"
                                        onclick="deleteArticle()">삭제
                                </button>
                            <li>
                                <a class="link-modify-article" href="/index.html">목록</a>
                            </li>
                        </ul>
                    </div>
                </article>

                <div class="qna-comment">
                    <div class="qna-comment-slipp">
                        <p class="qna-comment-count"><strong><%=article.replies().size()%>
                        </strong>개의 의견</p>
                        <div class="qna-comment-slipp-articles">
                            <%for (ReplyDto reply : article.replies()) { %>
                            <article class="article" id="answer-1405">
                                <div class="article-header">
                                    <div class="article-header-thumb">
                                        <img src="https://graph.facebook.com/v2.3/1324855987/picture"
                                             class="article-author-thumb" alt="">
                                    </div>
                                    <div class="article-header-text">
                                        <a href="<%="/user/" + reply.writerId()%>"
                                           class="article-author-name"><%=reply.writerId()%>
                                        </a>
                                        <a href="#answer-1434" class="article-header-time" title="퍼머링크">
                                            <%= reply.updatedAt()%>
                                        </a>
                                    </div>
                                </div>
                                <div class="article-doc comment-doc" style="white-space: pre-wrap;">
                                    <p><%= reply.contents().trim() %>
                                    </p>
                                </div>
                                <div class="article-util">
                                    <ul class="article-util-list">
                                        <li>
                                            <a class="link-modify-article"
                                               href="/questions/413/answers/1405/form">수정</a>
                                        </li>
                                        <li>
                                            <form class="delete-answer-form" action="/questions/413/answers/1405"
                                                  method="POST">
                                                <input type="hidden" name="_method" value="DELETE">
                                                <button type="submit" class="delete-answer-button">삭제</button>
                                            </form>
                                        </li>
                                    </ul>
                                </div>
                            </article>
                            <% }%>
                            <form class="submit-write" method="post" action=<%="/reply"%>>
                                <input type="hidden" id="articleId" name="articleId" value="<%=article.articleId()%>">
                                <div class="form-group" style="padding:14px;">
                                    <textarea class="form-control" id="contents" name="contents" rows="3"
                                              placeholder="Update your status"></textarea>
                                </div>
                                <button class="btn btn-success pull-right" type="submit">답변하기</button>
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

<script>
    document.addEventListener('DOMContentLoaded', (event) => {
        const articleDoc = document.querySelector('.article-doc');
        articleDoc.innerHTML = articleDoc.innerHTML.trim();
    });

    document.addEventListener('DOMContentLoaded', (event) => {
        const commentDoc = document.querySelector('.comment-doc');
        commentDoc.innerHTML = commentDoc.innerHTML.trim();
    });

    var articleId = <%=article.articleId()%>;

    function deleteArticle() {

        fetch(`/question/` + articleId, {
            method: 'DELETE',
        })
            .then(response => {
                if (response.status === 303) {
                    window.location.href = "/";
                } else if (response.ok) {
                    return response.text();
                } else {
                    throw new Error('Network response was not ok');
                }
            })
            .then(data => {
                console.log('Success:', data);
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    }
</script>

<!-- script references -->
<%@ include file="/WEB-INF/components/scirpt-refernces.html" %>
</body>
</html>
