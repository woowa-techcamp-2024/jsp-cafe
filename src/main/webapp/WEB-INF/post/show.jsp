<%@ page import="org.example.demo.domain.Post" %>
<%@ page import="org.example.demo.domain.Comment" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="kr" xmlns:jsp="http://java.sun.com/JSP/Page">


<jsp:include page="/components/header.jsp"/>
<body>
<jsp:include page="/components/nav.jsp"/>
<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <%
            Post post = (Post) request.getAttribute("post");
        %>
        <div class="panel panel-default">
            <header class="qna-header">
                <h2 class="qna-title"><%=post.getTitle()%>
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
                            <a href="/users/<%=post.getWriter().getId()%>"
                               class="article-author-name"><%=post.getWriter().getName()%>
                            </a>
                            <p>
                                <%=post.getCreatedAt()%>
                                <i class="icon-link"></i>
                            </p>
                        </div>
                    </div>
                    <div class="article-doc">
                        <%=post.getContents()%>
                    </div>
                    <div class="article-util">
                        <ul class="article-util-list">
                            <li>
                                <a class="link-modify-article" href="/posts/<%=post.getId()%>/edit">수정</a>
                            </li>
                            <li>
                                <form class="form-delete" action="/posts/<%=post.getId()%>" method="POST">
                                    <input type="hidden" name="_method" value="DELETE">
                                    <button class="link-delete-article" type="submit">삭제</button>
                                </form>
                            </li>
                            <li>
                                <a class="link-modify-article" href="/">목록</a>
                            </li>
                        </ul>
                    </div>
                </article>
                <%
                    List<Comment> comments = post.getComments();
                %>
                <div class="qna-comment">
                    <div class="qna-comment-slipp">
                        <%--                        <p class="qna-comment-count"><strong><%=comments.size()%></strong>개의 의견</p>--%>
                        <div class="qna-comment-slipp-articles">
                            <%
                                for (Comment comment : comments) {
                            %>
                            <article class="article" id="<%=comment.getId()%>">
                                <div class="article-header">
                                    <div class="article-header-thumb">
                                        <img src="https://graph.facebook.com/v2.3/1324855987/picture"
                                             class="article-author-thumb" alt="">
                                    </div>
                                    <div class="article-header-text">
                                        <a href="#" class="article-author-name"><%=comment.getWriter().getName()%>
                                        </a>
                                        <div class="article-header-time"><%=comment.getCreatedAt()%>
                                        </div>
                                    </div>
                                </div>
                                <div class="article-doc comment-doc">
                                    <p><%=comment.getContents()%>
                                    </p>
                                </div>
                                <ul class="article-util-list">
                                    <li>
                                        <form class="delete-answer-form"
                                              action="/posts/<%=post.getId()%>/comments/<%=comment.getId()%>"
                                              method="POST">
                                            <input type="hidden" name="_method" value="DELETE">
                                            <button type="submit" class="delete-answer-button">삭제</button>
                                        </form>
                                    </li>
                                </ul>
                            </article>
                            <%
                                }
                            %>
                            <form class="submit-write" action="/posts/<%=post.getId()%>/comments" method="post">
                                <div class="form-group" style="padding:14px;">
                                <textarea class="form-control" name="contents"
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
                    <form class="delete-answer-form" action="/posts/<%=post.getId()%>/comments/{3}" method="POST">
                        <input type="hidden" name="_method" value="DELETE">
                        <button type="submit" class="delete-answer-button">삭제</button>
                    </form>
                </li>
            </ul>
        </div>
    </article>
</script>

<jsp:include page="/components/footer.jsp"/>
</body>
</html>