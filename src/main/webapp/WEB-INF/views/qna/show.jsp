<%@ page import="com.woowa.cafe.dto.article.ArticleDto" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="com.woowa.cafe.dto.article.ReplyDto" %>
<%@ page import="java.util.Comparator" %>
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
                        <form class="submit-write">
                            <input type="hidden" id="articleId" name="articleId" value="<%=article.articleId()%>">
                            <div class="form-group" style="padding:14px;">
                                    <textarea class="form-control" id="contents" name="contents" rows="3"
                                              placeholder="Update your status"></textarea>
                            </div>
                            <button class="btn btn-success pull-right" type="button" id="submit-reply">답변하기
                            </button>
                            <div class="clearfix"/>
                        </form>
                        <p class="qna-comment-count"><strong><%=article.replyCount()%>
                        </strong>개의 의견</p>
                        <div class="qna-comment-slipp-articles">
                            <% for (ReplyDto reply : article.replies()) { %>
                            <article class="article" id="reply-<%=reply.replyId()%>">
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
                                        <%--                                        ---%>
                                        <li>
                                            <button type="submit" class="delete-answer-button"
                                                    data-reply-id="<%=reply.replyId()%>">삭제
                                            </button>
                                        </li>
                                    </ul>
                                </div>
                            </article>
                            <% }%>
                        </div>
                        <% if (article.replyCount() >= 6) { %>
                        <%Long replyMaxId = article.replies().stream().map(ReplyDto::replyId).max(Long::compareTo).orElse(0L);%>
                        <div>
                            <a href="/reply?articleId=<%=article.articleId()%>&index=<%=replyMaxId%>&size=5"
                               class="more-comment">더보기</a>
                        </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    var articleId = <%=article.articleId()%>;
    <% Long replyCount = 0L;
    if(article.replyCount() < 5) {
        replyCount = article.replyCount();
    } else {
        replyCount = 5L;
    }%>;
    var replyCount = <%=replyCount%>;

    document.addEventListener('DOMContentLoaded', function () {
        document.querySelector('.qna-comment-slipp-articles').addEventListener('click', function (e) {
            if (e.target && e.target.classList.contains('more-comment')) {
                e.preventDefault();
                const moreCommentLink = e.target;
                const url = moreCommentLink.href;

                fetch(url)
                    .then(response => response.json())
                    .then(data => {
                        const commentSection = document.querySelector('.qna-comment-slipp-articles');
                        data.forEach(reply => {
                            const replyTemplate = `
                    <article class="article" id="reply-` + reply.replyId + `">
                        <div class="article-header">
                            <div class="article-header-thumb">
                                <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">
                            </div>
                            <div class="article-header-text">
                                <a href="/user/` + reply.writerId + `" class="article-author-name">` + reply.writerName + `</a>
                                <a href="#answer-` + reply.replyId + `" class="article-header-time" title="퍼머링크">` + reply.updatedAt + `</a>
                            </div>
                        </div>
                        <div class="article-doc comment-doc" style="white-space: pre-wrap;">
                            <p>` + reply.contents.trim() + `</p>
                        </div>
                        <div class="article-util">
                            <ul class="article-util-list">
                                <li>
                                    <button type="button" class="delete-answer-button" data-reply-id="` + reply.replyId + `">삭제</button>
                                </li>
                            </ul>
                        </div>
                    </article>`;
                            commentSection.insertAdjacentHTML('beforeend', replyTemplate);
                        });

                        replyCount += data.length;

                        if (data.length < 5) {
                            moreCommentLink.remove();
                        } else {
                            const lastReplyId = data[data.length - 1].replyId;
                            moreCommentLink.href = `/reply?articleId=` + articleId + `&index=` + lastReplyId + `&size=5`;
                        }
                    })
                    .catch(error => {
                        console.error("Error fetching more comments:", error);
                        alert("Error fetching more comments: " + error);
                    });
            }
        });
    });


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
                    return response.json().then(errorData => {
                        const status = errorData.status;
                        const message = errorData.detailMessage;
                        console.log("Error deleting article: ", status, message);
                        throw new Error(`Error: ` + status + `: ` + message);
                    });
                }
            }).catch((error) => {
            alert(error.message);
        });
    }


    document.addEventListener('DOMContentLoaded', function () {
        console.log("Document is ready");

        // 답변하기 버튼 클릭 이벤트
        document.getElementById('submit-reply').addEventListener('click', function (e) {
            console.log("Reply button clicked");
            e.preventDefault();

            var form = document.querySelector('.submit-write');
            var formData = new URLSearchParams(new FormData(form)).toString();

            fetch('/reply', {
                headers: {
                    'content-type': 'application/x-www-form-urlencoded'
                },
                method: 'POST',
                body: formData
            })
                .then(response => response.json())
                .then(data => {
                        data.contents = data.contents.replace(/\n/g, "<br>");
                        var replyMore = `<a href="`
                        var replyTemplate = `
                <article class="article" id="reply-` + data.replyId + `">
                    <div class="article-header">
                        <div class="article-header-thumb">
                            <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">
                        </div>
                        <div class="article-header-text">
                            <a href="/user/` + data.writerId + `" class="article-author-name">` + data.writerName + `</a>
                            <a href="#answer-` + data.replyId + `" class="article-header-time" title="퍼머링크">` + data.updatedAt + `</a>
                        </div>
                    </div>
                    <div class="article-doc comment-doc">
                        <p>` + data.contents + `</p>
                    </div>
                    <div class="article-util">
                        <ul class="article-util-list">
                            <li>
                                <button type="button" class="delete-answer-button" data-reply-id="` + data.replyId + `">삭제</button>
                            </li>
                        </ul>
                    </div>
                </article>`;
                        var replyMore = `<div>
                        <a href="/reply?articleId=` + <%=article.articleId()%> +`&index=` + (data.replyId - 1) + `&size=5" class="more-comment">더보기</a>
                        </div>`;
                        var commentCount = document.querySelector(".qna-comment-count strong");

                        commentCount.textContent = parseInt(commentCount.textContent) + 1;
                        console.log("replyCount: ", replyCount);
                        if (replyCount % 5 !== 0 || replyCount === 0) {
                            document.querySelector(".qna-comment-slipp-articles").insertAdjacentHTML('beforeend', replyTemplate);
                        } else {
                            var moreComment = document.querySelector(".more-comment");
                            if (moreComment) {
                                moreComment.remove();
                            }
                            document.querySelector(".qna-comment-slipp-articles").insertAdjacentHTML('beforeend', replyMore);
                        }
                        replyCount++;
                        document.getElementById("contents").value = "";
                    }
                )
                .catch(error => {
                    console.error("Error adding reply:", error);
                    alert("Error adding reply: " + error);
                });
        });

        // 댓글 삭제 버튼 클릭 이벤트
        document.querySelector(".qna-comment-slipp-articles").addEventListener('click', function (e) {
            if (e.target.classList.contains('delete-answer-button')) {
                e.preventDefault();

                var deleteBtn = e.target;
                var replyId = deleteBtn.getAttribute('data-reply-id');
                var url = `/reply/` + replyId;
                console.log("url: ", url);

                fetch(url, {
                    method: 'DELETE'
                }).then(response => {
                    if (response.ok) {
                        deleteElementById("reply-" + replyId);
                        var commentCount = document.querySelector(".qna-comment-count strong");
                        commentCount.textContent = parseInt(commentCount.textContent) - 1;
                    } else {
                        console.error("Error deleting reply");
                        alert("Error deleting reply");
                    }
                })
                    .catch(error => {
                        console.error("Error deleting reply:", error);
                        alert("Error deleting reply");
                    });
            }
        });

        // 내용 trim 처리
        document.querySelectorAll('.article-doc, .comment-doc').forEach(function (el) {
            el.innerHTML = el.innerHTML.trim();
        });
    });
</script>

<!-- script references -->
<%@ include file="/WEB-INF/components/scirpt-refernces.html" %>
</body>
</html>
