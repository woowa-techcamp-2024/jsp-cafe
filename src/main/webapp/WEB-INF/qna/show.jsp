<%@ page import="codesquad.javacafe.post.dto.response.PostResponseDto" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page buffer="8kb" autoFlush="true" %>

<!DOCTYPE html>
<html lang="kr">
<jsp:include page="/common/header.jsp"/>
<body>
<jsp:include page="/common/topbar.jsp"/>
<jsp:include page="/common/navbar.jsp"/>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
            <%
                var post = (PostResponseDto) request.getAttribute("post");
            %>
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
                            <a href="#" class="article-author-name"><%=post.getWriter()%>
                            </a>
                            <a href="#" class="article-header-time" title="퍼머링크">
                                <%=post.getCreatedAt()%>
                                <i class="icon-link"></i>
                            </a>
                        </div>
                    </div>
                    <div class="article-doc">
                        <%
                            var contents = post.getContents().split("\n");
                            for (var content : contents) {

                        %><p><%=content%>
                    </p>
                        <%
                            }
                        %>
                    </div>
                    <div class="article-util">
                        <ul class="article-util-list">
                            <li>
                                <a class="link-modify-article" id="updateButton"
                                   href="/api/post/update?postId=<%=post.getId()%>">수정</a>
                            </li>
                            <li>
                                <form class="form-delete" action="/api/post" method="POST">
                                    <input type="hidden" name="method" value="DELETE">
                                    <input type="hidden" name="postId" id="postId" value="<%=post.getId()%>">
                                    <input type="hidden" name="memberId" id="memberId" value="<%=post.getMemberId()%>">
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
                        <p class="qna-comment-count"><strong>2</strong>개의 의견</p>
                        <div class="qna-comment-slipp-articles">

                            <article class="article" id="answer-1405">
                                <div class="article-header">
                                    <div class="article-header-thumb">
                                        <img src="https://graph.facebook.com/v2.3/1324855987/picture"
                                             class="article-author-thumb" alt="">
                                    </div>
                                    <div class="article-header-text">
                                        <a href="/users/1/자바지기" class="article-author-name">자바지기</a>
                                        <a href="#answer-1434" class="article-header-time" title="퍼머링크">
                                            2016-01-12 14:06
                                        </a>
                                    </div>
                                </div>
                                <div class="article-doc comment-doc">
                                    <p>이 글만으로는 원인 파악하기 힘들겠다. 소스 코드와 설정을 단순화해서 공유해 주면 같이 디버깅해줄 수도 있겠다.</p>
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

                        </div>
                        <form class="submit-write">
                            <div class="form-group" style="padding:14px;">
                                    <textarea class="form-control" id="comment"
                                              placeholder="Update your status"></textarea>
                            </div>
                            <button class="btn btn-success pull-right" id="commentButton" type="button"
                                    onclick="postComment()">답변하기
                            </button>
                            <div class="clearfix"></div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<script>
    function postComment() {
        console.log("create comment called");
        $.ajax({
            url: '/ajax/comment',
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify({
                postId: $('#postId').val(),
                memberId: $('#memberId').val(),
                comment: $('#comment').val()
            }),
            timeout: 1500,
            success: function (data) {
                console.log(data);
                loadCommentList();
            },
            error: function (request, status, error) {

                console.log(error);
            }
        });

        document.getElementById('comment').value = '';
    }

    function addCommentToPage(commentData) {
        const container = document.querySelector('.qna-comment-slipp-articles');
        container.innerHTML = '';



        var template = '';
        commentData.forEach(comment=> {
            console.log(comment);
            var addTemplate = $('#answerTemplate').html();
            var formattedComment = addTemplate
                .replace('{authorName}', comment.member.name) // Assuming memberId is used as authorName
                .replace('{createdAt}', comment.createdAt)
                .replace('{comment}', comment.comment);

            template += formattedComment;
        });

        console.log(template);
        // container.appendChild(template);
        // $('#answer-1405').append(template);
        $('.qna-comment-slipp-articles').append(template);
    }
</script>

<script>
    window.onload = function () {
        loadCommentList();
    }

    function loadCommentList(){
        $.ajax({
            url: '/ajax/comment?postId='+$('#postId').val(),
            type: 'GET',
            dataType: 'json',
            contentType: 'application/json',
            timeout: 1500,
            success: function (data) {
                console.log(data);
                addCommentToPage(data);
            },
            error: function (request, status, error) {
                console.log(error);

            }
        });
    }
</script>

<script type="text/template" id="answerTemplate">
    <article class="article">
        <div class="article-header">
            <div class="article-header-thumb">
                <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">
            </div>
            <div class="article-header-text">
                <a href="#" class="article-author-name">{authorName}</a>
                <div class="article-header-time">{createdAt}</div>
            </div>
        </div>
        <div class="article-doc comment-doc">
            {comment}
        </div>
        <div class="article-util">
            <ul class="article-util-list">
                <li>
                    <a class="link-modify-article" href="#">수정</a>
                </li>
                <li>
                    <form class="delete-answer-form" action="#" method="POST">
                        <input type="hidden" name="_method" value="DELETE">
                        <button type="submit" class="delete-answer-button">삭제</button>
                    </form>
                </li>
            </ul>
        </div>
    </article>
</script>

<!-- script references -->
<script src="<%= request.getContextPath() %>/js/jquery-2.2.0.min.js"></script>
<script src="<%= request.getContextPath() %>/js/bootstrap.min.js"></script>
<script src="<%= request.getContextPath() %>/js/scripts.js"></script>
</body>
</html>
