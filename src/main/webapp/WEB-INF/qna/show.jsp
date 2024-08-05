<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<%@include file="/WEB-INF/share/header.jsp" %>
<body>
<%@include file="/WEB-INF/share/navbar.jsp" %>
<%@include file="/WEB-INF/share/sub_navbar.jsp" %>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
            <header class="qna-header">
                <h2 class="qna-title"><c:out value="${article.title}"/></h2>
            </header>
            <div class="content-main">
                <article class="article">
                    <div class="article-header">
                        <div class="article-header-thumb">
                            <img src="https://graph.facebook.com/v2.3/100000059371774/picture"
                                 class="article-author-thumb" alt="">
                        </div>
                        <div class="article-header-text">
                            <a href="/users/profile/<c:out value="${article.author.id}"/>" class="article-author-name">
                                <c:out value=" ${article.author.name}"/>
                            </a>
                            <a href="/todo" class="article-header-time" title="퍼머링크">
                                ${article.createdAt}
                                <i class="icon-link"></i>
                            </a>
                        </div>
                    </div>
                    <div class="article-doc" style="margin-top: 28px;">
                        <c:out value="${article.contents}"/>
                    </div>
                    <c:if test="${article.isMine}">
                        <div class="article-util">
                            <ul class="article-util-list">
                                <li>
                                    <a class="link-modify-article"
                                       href="/qna/<c:out value="${article.id}"/>/form">수정</a>
                                </li>
                                <li>
                                    <form class="form-delete" action="/qna/<c:out value="${article.id}"/>"
                                          method="POST">
                                        <input type="hidden" name="_method" value="DELETE">
                                        <button class="link-delete-article" type="submit">삭제</button>
                                    </form>
                                </li>
                                <li>
                                    <a class="link-modify-article" href="/">목록</a>
                                </li>
                            </ul>
                        </div>
                    </c:if>
                </article>
                <div class="qna-comment">
                    <div class="qna-comment-slipp">
                        <p class="qna-comment-count"><strong id="commentCount">0</strong>개의 의견</p>
                        <form class="submit-write answer-write" name="answer" method="POST"
                              action="/qna/${article.id}/replies">
                            <div class="form-group" style="padding:14px;">
                                <textarea name="contents" id="contents" class="form-control"
                                          placeholder="답변을 작성해보세요."></textarea>
                            </div>
                            <button class="btn btn-success pull-right" type="submit">답변하기</button>
                            <div class="clearfix"></div>
                        </form>
                        <div id="qna-comment-slipp-articles" class="qna-comment-slipp-articles">
                            <!-- 댓글이 여기에 동적으로 추가됩니다 -->
                        </div>
                        <button id="loadMoreButton" class="btn btn-primary" >댓글 더보기</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="/WEB-INF/share/footer.jsp" %>


<script type="text/template" id="answerTemplate">
    <article id="reply-{0}" class="article">
        <div class="article-header">
            <div class="article-header-thumb">
                <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">
            </div>
            <div class="article-header-text">
                <a href="#" class="article-author-name">{1}</a>
                <div class="article-header-time">{2}</div>
            </div>
        </div>
        <div class="article-doc comment-doc">
            {3}
        </div>
        <div class="article-util">
            <ul class="article-util-list">
                <li>
                    <a class="link-modify-article" href="/api/qna/updateAnswer/${article.id}">수정</a>
                </li>
                <li>
                    <form class="delete-answer-form" action="/api/questions/${article.id}/answers/{4}">
                        <button type="submit" class="delete-answer-button">삭제</button>
                    </form>
                </li>
            </ul>
        </div>
    </article>
</script>

<script>
    let currentPointer = 0;
    const articleId = ${article.id}; // JSP에서 articleId를 가져옵니다

    function loadComments() {
        $.ajax({
            url: `/qna/\${articleId}/replies?pointer=\${currentPointer}`,
            method: 'GET',
            dataType: 'json',
            success: function(response) {
                renderComments(response.elements);
                updateCommentCount(response.totalElementsCount);
                toggleLoadMoreButton(response.hasNext);
                if (response.elements.length > 0) {
                    currentPointer = response.elements[response.elements.length - 1].id;
                }
            },
            error: function(xhr, status, error) {
                console.error("댓글을 불러오는데 실패했습니다:", error);
            }
        });
    }

    function renderComments(comments) {
        const $commentContainer = $('.qna-comment-slipp-articles');
        const template = $('#answerTemplate').html();

        comments.forEach(comment => {
            const renderedTemplate = template.format(comment.id, comment.author.name, comment.createdAt, comment.contents, comment.id);
            $commentContainer.append(renderedTemplate);
        });
    }

    function updateCommentCount(count) {
        $('.qna-comment-count strong').text(count);
    }

    function toggleLoadMoreButton(hasNext) {
        console.log(hasNext)
        $('#loadMoreButton').toggle(hasNext);
    }

    loadComments();

    $('#loadMoreButton').on('click', function() {
        loadComments();
    });


    $(".submit-write button[type=submit]").click(addAnswer);

    function addAnswer(e) {
        e.preventDefault();

        var url = $(".answer-write").attr("action");

        var contents = $("#contents").val();
        var data = JSON.stringify({contents: contents});

        $.ajax({
            type: 'POST',
            url: url,
            data: data,
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            error: function () {
                alert("error");
            },
            success: function (data, status) {
                var answerTemplate = $("#answerTemplate").html();
                var template = answerTemplate.format(data.id, data.author.name, data.createdAt, data.contents, data.id);
                $(".qna-comment-slipp-articles").append(template);
                $("textarea[name=contents]").val("");

                var tagId = "reply-" + data.id;
                window.location.hash = tagId;

                let commentCount = getCommentCount();
                updateCommentCount(commentCount + 1);
                $("#" + tagId).css("background-color", "yellow");
                setTimeout(function () {
                    $("#" + tagId).css("background-color", "");
                }, 2000); // 2초 후 원래 배경색으로 돌아옴
                // 해당 댓글로 이동
            }
        });
    }

    function getCommentCount() {
        const countText = $('.qna-comment-count strong').text();
        return parseInt(countText, 10) || 0;
    }

    $(".delete-answer-form button[type='submit']").click(deleteAnswer);

    function deleteAnswer(e) {
        e.preventDefault();

        var deleteBtn = $(this);
        var url = $(".delete-answer-form").attr("action");

        $.ajax({
            type: 'DELETE',
            url: url,
            dataType: 'json',
            error: function (xhr, status) {
                console.log("error");
            },
            success: function (data, status) {
                console.log('data:' + data);
                deleteBtn.closest("article").remove();
            }
        });
    }
</script>
</body>
</html>