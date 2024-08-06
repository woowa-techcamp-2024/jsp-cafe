<%@ page import="com.example.entity.Article, com.example.entity.Reply" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: woowatech04
  Date: 2024. 7. 23.
  Time: 오후 2:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="kr">
<%@ include file="../template/head.jsp" %>
<body>
<%@ include file="../template/top-header.jsp" %>
<%@ include file="../template/sub-header.jsp" %>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
            <%
                Article article = (Article) request.getAttribute("article");
            %>
            <header class="qna-header">
                <h2 class="qna-title"><%= article.getTitle() %></h2>
            </header>
            <div class="content-main">
                <article class="article">
                    <div class="article-header">
                        <div class="article-header-thumb">
                            <img src="https://graph.facebook.com/v2.3/100000059371774/picture"
                                 class="article-author-thumb" alt="">
                        </div>
                        <div class="article-header-text">
                            <a href="/users/profile/<%= article.getUserId() %>"
                               class="article-author-name"><%= article.getUserName() %></a>
                            <a href="/questions/413" class="article-header-time" title="퍼머링크">
                                <%= article.getCreatedAt()%>
                                <i class="icon-link"></i>
                            </a>
                        </div>
                    </div>
                    <div class="article-doc">
                        <%= article.getContents() %>
                    </div>
                    <div class="article-util">
                        <ul class="article-util-list">
                            <li>
                                <a class="link-modify-article"
                                   href="${pageContext.request.contextPath}/articles/edit/<%= article.getId() %>">수정</a>
                            </li>
                            <li>
                                <form class="form-delete" action="/articles/<%= article.getId() %>" method="POST">
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

                <div class="qna-comment">
                    <div class="qna-comment-slipp">
                        <p class="qna-comment-count"><strong id="comment-count"><%= request.getAttribute("replyCount") %></strong>개의 의견</p>
                        <div class="qna-comment-slipp-articles" id="comment-list">
                            <%
                                List<Reply> replies = (List<Reply>) request.getAttribute("replies");
                                int count = 0;
                                for (Reply reply : replies) {
                                    count++;
                                    if (count == 6) {
                                        break;
                                    }
                            %>
                            <article class="article" id="comment-<%= reply.getId() %>">
                                <div class="article-header">
                                    <div class="article-header-thumb">
                                        <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">
                                    </div>
                                    <div class="article-header-text">
                                        <a href="#" class="article-author-name"><%= reply.getUserName() %></a>
                                        <div class="article-header-time"><%= reply.getCreatedAt() %></div>
                                    </div>
                                </div>
                                <div class="article-doc comment-doc">
                                    <%= reply.getContents() %>
                                </div>
                                <div class="article-util">
                                    <ul class="article-util-list">
                                        <li>
                                            <button class="delete-comment-button" data-id="<%= reply.getId() %>">삭제</button>
                                        </li>
                                    </ul>
                                </div>
                            </article>
                            <% } %>
                        </div>
                        <button id="load-more-comments" class="btn btn-primary">더보기</button>
                        <form id="comment-form" class="submit-write">
                            <div class="form-group" style="padding:14px;">
                                <textarea id="comment-content" class="form-control" placeholder="Update your status"></textarea>
                            </div>
                            <button id="submit-comment" class="btn btn-success pull-right" type="button">답변하기</button>
                            <div class="clearfix"></div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/template" id="comment-template">
    <article class="article" id="comment-{commentId}">
        <div class="article-header">
            <div class="article-header-thumb">
                <img src="{authorThumb}" class="article-author-thumb" alt="">
            </div>
            <div class="article-header-text">
                <a href="#" class="article-author-name">{userName}</a>
                <div class="article-header-time">{createdAt}</div>
            </div>
        </div>
        <div class="article-doc comment-doc">
            {contents}
        </div>
        <div class="article-util">
            <ul class="article-util-list">
                <li>
                    <button class="delete-comment-button" data-id="{commentId}">삭제</button>
                </li>
            </ul>
        </div>
    </article>
</script>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
    $(document).ready(function () {
        function updateCommentCount() {
            const count = document.getElementById('comment-count');
            console.log(count);
            let currentCount = parseInt(count.textContent);
            if (!isNaN(currentCount)) {
                currentCount += 1;
                count.textContent = currentCount;
            }
        }

        function decreaseCommentCount() {
            const count = document.getElementById('comment-count');
            count.value = count.value - 1;
        }

        function addComment(comment, appendToBottom = false) {
            var template = $('#comment-template').html();
            template = template.replace(/{commentId}/g, comment.id)
                .replace('{authorThumb}', 'https://graph.facebook.com/v2.3/1324855987/picture')
                .replace('{userName}', comment.userName)
                .replace('{createdAt}', comment.createdAt)
                .replace('{contents}', comment.contents);
            if (appendToBottom) {
                $('#comment-list').append(template);
            } else {
                $('#comment-list').prepend(template);
            }
            // updateCommentCount();
        }

        function loadComments(lastCreatedAt, lastReplyId) {
            $.ajax({
                url: '/replies',
                method: 'GET',
                data: {
                    articleId: '<%= article.getId() %>',
                    lastCreatedAt: lastCreatedAt,
                    lastReplyId: lastReplyId
                },
                success: function (response) {
                    response.slice(0, 5).forEach(function(comment) {
                        addComment(comment, true);
                    });
                    if (response.length < 6) {
                        $('#load-more-comments').hide();
                    }
                },
                error: function () {
                    alert('댓글을 불러오는데 실패했습니다.');
                }
            });
        }

        $('#submit-comment').on('click', function () {
            var content = $('#comment-content').val();
            if (content.trim() === '') return;

            $.ajax({
                url: '/replies',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ articleId: '<%= article.getId() %>', content: content }),
                success: function (response) {
                    if ($('#load-more-comments').is(':visible')) {
                        // If there are more comments to load, do not append the new comment
                        $('#comment-content').val('');
                    } else {
                        // Otherwise, append the new comment to the bottom
                        addComment(response, true);
                        $('#comment-content').val('');
                    }
                    updateCommentCount();
                },
                error: function () {
                    alert('댓글 추가에 실패했습니다.');
                }
            });
        });

        $('#comment-list').on('click', '.delete-comment-button', function () {
            var commentId = $(this).data('id');
            $.ajax({
                url: '/replies/' + commentId,
                method: 'DELETE',
                success: function () {
                    $('#comment-' + commentId).remove();
                    decreaseCommentCount();
                },
                error: function () {
                    alert('댓글 삭제에 실패했습니다.');
                }
            });
        });

        $('#load-more-comments').on('click', function () {
            var lastComment = $('#comment-list .article').last();
            var lastCreatedAt = lastComment.find('.article-header-time').text();
            var lastReplyId = lastComment.attr('id').split('-')[1];
            loadComments(lastCreatedAt, lastReplyId);
        });
    });
</script>

<!-- script references -->
<%@include file="../template/footer.jsp" %>
</body>
</html>
