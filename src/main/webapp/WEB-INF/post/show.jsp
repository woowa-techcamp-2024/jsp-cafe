<%@ page import="org.example.demo.domain.Post" %>
<%@ page import="org.example.demo.domain.Comment" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
                <h2 class="qna-title"><c:out value="${post.title}" /></h2>
            </header>
            <div class="content-main">
                <article class="article">
                    <div class="article-header">
                        <div class="article-header-thumb">
                            <img src="https://graph.facebook.com/v2.3/100000059371774/picture"
                                 class="article-author-thumb" alt="">
                        </div>
                        <div class="article-header-text">
                            <a href="/users/<c:out value="${post.writer.id}" />" class="article-author-name"><c:out value="${post.writer.name}" /></a>
                            <p>
                                <c:out value="${post.createdAt}" />
                                <i class="icon-link"></i>
                            </p>
                        </div>
                    </div>
                    <div class="article-doc">
                        <c:out value="${fn:replace(post.contents, '\n', '<br />')}" escapeXml="false" />
                    </div>
                    <div class="article-util">
                        <ul class="article-util-list">
                            <li>
                                <a class="link-modify-article" href="/posts/<c:out value="${post.id}" />/edit">수정</a>
                            </li>
                            <li>
                                <form class="form-delete" action="/posts/<c:out value="${post.id}" />" method="POST">
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
                        <%-- <p class="qna-comment-count"><strong><%=comments.size()%></strong>개의 의견</p> --%>
                        <div class="qna-comment-slipp-articles">
                            <%
                                for (Comment comment : comments) {
                            %>
                            <article class="article" id="<c:out value="${comment.id}" />">
                                <div class="article-header">
                                    <div class="article-header-thumb">
                                        <img src="https://graph.facebook.com/v2.3/1324855987/picture"
                                             class="article-author-thumb" alt="">
                                    </div>
                                    <div class="article-header-text">
                                        <a href="#" class="article-author-name"><c:out value="${comment.writer.name}" /></a>
                                        <div class="article-header-time"><c:out value="${comment.createdAt}" /></div>
                                    </div>
                                </div>
                                <div class="article-doc comment-doc">
                                    <p><c:out value="${fn:replace(comment.contents, '\n', '<br />')}" escapeXml="false" /></p>
                                </div>
                                <ul class="article-util-list">
                                    <li>
                                        <form class="delete-answer-form"
                                              action="/posts/<c:out value="${post.id}" />/comments/<c:out value="${comment.id}" />"
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
                            <form class="submit-write" action="/posts/<c:out value="${post.id}" />/comments" method="post">
                                <div class="form-group" style="padding:14px;">
                                    <textarea class="form-control" name="contents" placeholder="Update your status"></textarea>
                                </div>
                                <button class="btn btn-success pull-right" type="submit">답변하기</button>
                                <div class="clearfix"></div>
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
                    <form class="delete-answer-form" action="/posts/<c:out value="${post.id}" />/comments/{3}" method="POST">
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
