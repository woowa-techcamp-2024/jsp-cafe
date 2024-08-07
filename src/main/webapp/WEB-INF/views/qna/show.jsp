<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.StringTokenizer" %>
<%@ page import="codesquad.article.handler.dto.response.ArticleResponse" %>
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
<%@include file="/WEB-INF/component/header/header.jsp" %>
<%@include file="/WEB-INF/component/navigation/navigation.jsp" %>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <%
            ArticleResponse article = (ArticleResponse) request.getAttribute("articleResponse");
            StringTokenizer st = new StringTokenizer(article.content(), "\n");
        %>
        <div class="panel panel-default">
            <header class="qna-header">
                <h2 class="qna-title"><%=article.title()%>
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
                            <a href="/users/<%=article.writerId()%>" class="article-author-name"><%=article.writer()%>
                            </a>
                            <a href="/questions/413" class="article-header-time" title="퍼머링크">
                                2015-12-30 01:47
                                <i class="icon-link"></i>
                            </a>
                        </div>
                    </div>
                    <div class="article-doc">
                        <%
                            while (st.hasMoreElements()) {
                        %>
                        <p><%=st.nextToken()%>
                        </p>
                        <%
                            }
                        %>
                    </div>
                    <div class="article-util">
                        <ul class="article-util-list">
                            <li>
                                <a class="link-modify-article"
                                   href="/questions/<%=article.articleId()%>/update-form">수정</a>
                            </li>
                            <li>
                                <form class="form-delete" action="/questions/<%=article.articleId()%>" method="POST">
                                    <input type="hidden" name="_method" value="DELETE">
                                    <button class="link-delete-article" type="submit">삭제</button>
                                </form>
                            </li>
                            <li>
                                <a class="link-modify-article" href="/WEB-INF/views/index.jsp">목록</a>
                            </li>
                        </ul>
                    </div>
                </article>

                <div class="qna-comment">
                    <div class="qna-comment-slipp">
                        <p class="qna-comment-count"><strong id="comment-count">0</strong>개의 의견</p>
                        <div class="qna-comment-slipp-articles" id="comment-list"></div>
                        <button id="load-more-comments" class="btn btn-primary">더보기</button>
                        <form class="submit-write" id="comment-form" action="/questions/<%=article.articleId()%>/answers" method="POST">
                            <div class="form-group" style="padding:14px;">
                                <textarea name="contents" id="contents" class="form-control" placeholder="Update your status"></textarea>
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

<script type="text/template" id="commentTemplate">
    <article class="article" id="comment-{3}">
        <div class="article-header">
            <div class="article-header-thumb">
                <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">
            </div>
            <div class="article-header-text">
                <a href="/users/{0}" class="article-author-name">{1}</a>
                <div class="article-header-time">{2}</div>
            </div>
        </div>
        <div class="article-doc comment-doc">
            <p>{4}</p>
        </div>
        <div class="article-util">
            <ul class="article-util-list">
                <li>
                    <a class="link-modify-article" href="/questions/{5}/answers/{3}/update-form">수정</a>
                </li>
                <li>
                    <form class="delete-answer-form" action="/questions/{5}/answers/{3}" method="POST">
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
<script>
    $(document).ready(function () {
        let pageNumber = 1;
        const pageSize = 5;

        function loadComments(page) {
            const articleId = '<%=article.articleId()%>';
            $.ajax({
                type: 'GET',
                url: '/questions/' + articleId + '/answers',
                data: { pageNumber: page, pageSize: pageSize },
                success: function(response) {
                    const responseData = typeof response === 'string' ? JSON.parse(response) : response;
                    const comments = responseData.data.content;
                    const pageInfo = responseData.data.pageInfo;
                    if (pageInfo.isLast) {
                        $('#load-more-comments').hide();
                    } else {
                        $('#load-more-comments').show();
                    }
                    $('#comment-count').text(pageInfo.totalElements);
                    comments.forEach(comment => {
                        const template = $('#commentTemplate').html()
                            .replace('{0}', comment.commenterId)
                            .replace('{1}', comment.commenter)
                            .replace('{2}', new Date().toISOString().slice(0, 19).replace('T', ' '))
                            .replace('{3}', comment.id)
                            .replace('{4}', comment.content)
                            .replace('{5}', articleId);
                        $('#comment-list').append(template);
                    });
                },
                error: function () {
                    alert('Error loading comments.');
                }
            });
        }

        // Load initial comments
        loadComments(pageNumber);

        // Load more comments on button click
        $('#load-more-comments').on('click', function () {
            pageNumber++;
            loadComments(pageNumber);
        });

        $('#comment-form').on('submit', function (e) {
            e.preventDefault();
            const articleId = '<%=article.articleId()%>';
            const contents = $('#contents').val();
            $.ajax({
                type: 'POST',
                url: '/questions/' + articleId + '/answers',
                data: {contents: contents},
                success: function(response) {
                    const responseData = JSON.parse(response);
                    const comment = responseData.data;
                    const template = $('#commentTemplate').html()
                        .replaceAll('{0}', comment.commenterId)
                        .replaceAll('{1}', comment.commenter)
                        .replaceAll('{2}', new Date().toISOString().slice(0, 19).replace('T', ' '))
                        .replaceAll('{3}', comment.id)
                        .replaceAll('{4}', comment.content)
                        .replaceAll('{5}', articleId);
                    $('#comment-list').append(template);
                    $('#contents').val('');
                },
                error: function () {
                    alert('Error submitting comment.');
                }
            });
        });

        $(document).on('submit', '.delete-answer-form', function (e) {
            e.preventDefault();
            const form = $(this);
            const commentId = form.closest('.article').attr('id').split('-')[1];
            $.ajax({
                type: 'POST',
                url: form.attr('action'),
                data: {_method: 'DELETE'},
                success: function () {
                    $('#comment-' + commentId).remove();
                },
                error: function () {
                    alert('Error deleting comment.');
                }
            });
        });
    });
</script>
</body>
</html>
