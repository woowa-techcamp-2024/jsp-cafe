<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.codesquad.cafe.db.domain.PostDetail" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
            <header class="qna-header">
                <h2 class="qna-title">${post.title}</h2>
            </header>
            <div class="content-main">
                <article clakjss="article">
                    <div class="article-header">
                        <div class="article-header-thumb">
                            <img src="https://graph.facebook.com/v2.3/100000059371774/picture"
                                 class="article-author-thumb" alt="">
                        </div>
                        <div class="article-header-text">
                            <a href="/users/${post.authorId}" class="article-author-name">${post.authorUsername}</a>
                            <a href="/posts/${post.postId}" class="article-header-time" title="퍼머링크">
                                ${post.formattedDate}
                                <i class="icon-link"></i>
                            </a>
                        </div>
                    </div>
                    <div class="article-doc">
                        <%
                            String content = ((PostDetail) request.getAttribute("post")).getContent();
                            content = content.replaceAll("(\r\n|\n)", "<br>");
                        %>
                        <c:out value="<%= content %>" escapeXml="false"/>
                    </div>
                    <c:if test="${not empty sessionScope.userPrincipal and sessionScope.userPrincipal.id == post.authorId}">
                        <div class="article-util">
                            <ul class="article-util-list">
                                <li>
                                    <button type="submit" onclick="httpPost('/posts/' + ${post.postId})"
                                            class="delete-answer-button">
                                        수정
                                    </button>
                                </li>
                                <li>
                                    <button type="submit" onclick="deletePost()" class="delete-answer-button">
                                        삭제
                                    </button>
                                </li>
                            </ul>
                        </div>
                    </c:if>
                </article>

                <div class="qna-comment">
                    <div class="qna-comment-slipp">
                        <p class="qna-comment-count"><strong>${post.comments.size()}</strong>개의 의견</p>
                        <div class="qna-comment-slipp-articles">
                            <c:forEach var="comment" items="${post.comments}" varStatus="status">
                                <article class="article" id="comment-${comment.id}">
                                    <div class="article-header">
                                        <div class="article-header-thumb">
                                            <img src="https://graph.facebook.com/v2.3/1324855987/picture"
                                                 class="article-author-thumb" alt="">
                                        </div>
                                        <div class="article-header-text">
                                            <a href="/users/${comment.userId}"
                                               class="article-author-name">${comment.username}</a>
                                            <a href="#answer-1434" class="article-header-time" title="퍼머링크">
                                                    ${comment.formattedDate}
                                            </a>
                                        </div>
                                    </div>
                                    <div class="article-doc comment-doc">
                                        <p>${comment.content}</p>
                                    </div>
                                    <div class="article-util">
                                        <ul class="article-util-list">
                                        </ul>
                                    </div>
                                    <div class="article-util">
                                        <ul class="article-util-list">
                                            <li>
                                                <a class="link-modify-article">수정</a>
                                            </li>
                                            <li>
                                                <button type="submit" class="delete-answer-button"
                                                        onclick="deleteComment(${comment.id})">
                                                    삭제
                                                </button>
                                            </li>
                                        </ul>
                                    </div>
                                </article>
                            </c:forEach>
                        </div>
                        <form class="submit-write" onsubmit="postComment(event)">
                            <input type="hidden" id="postId" name="postId" value="${post.postId}"/>
                            <div class="form-group" style="padding:14px;">
                                    <textarea class="form-control" id="content" name="content"
                                              placeholder="댓글 작성"></textarea>
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
<script>
    async function postComment(event) {
        try {
            const data = await httpPost('/comments', event);
            console.log('서버 응답:', data);
            const newComment = document.createElement('article');
            newComment.classList.add('article');
            newComment.id = 'comment-' + data.id;
            newComment.innerHTML = `
                <div class="article-header">
                    <div class="article-header-thumb">
                        <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">
                    </div>
                    <div class="article-header-text">
                        <a href="/users/` + data.userId + `" class="article-author-name">` + data.username + `</a>
                        <a href="#answer-1434" class="article-header-time" title="퍼머링크">` + data.createdAt + `</a>
                    </div>
                </div>
                <div class="article-doc comment-doc">
                    <p>` + data.content + `</p>
                </div>
                <div class="article-util">
                     <ul class="article-util-list">
                                            <li>
                                                <a class="link-modify-article">수정</a>
                                            </li>
                                            <li>
                                                <button type="submit" class="delete-answer-button"
                                                        onclick="deleteComment(` + data.id + `)">
                                                    삭제
                                                </button>
                                            </li>
                                        </ul>
                </div>
            `;
            document.querySelector('.qna-comment-slipp-articles').appendChild(newComment);
        } catch (errorMessage) {
            console.log('에러 메시지:', errorMessage);
        }
        document.getElementById('content').value = '';
    }

    async function deletePost() {
        try {
            const data = await httpDelete('/posts/' + ${post.postId});
            console.log('서버 응답:', data);
            window.location.href = data['redirect'];
        } catch (errorMessage) {
            console.log('에러 메시지:', errorMessage);
            alert(errorMessage);
        }
    }

    async function deleteComment(commentId) {
        try {
            const data = await httpDelete('/comment/' + commentId);
            document.getElementById('comment-' + commentId).remove();
        } catch (errorMessage) {
            console.log('에러 메시지:', errorMessage);
            alert(errorMessage);
        }
    }
</script>
<%@ include file="/WEB-INF/views/include/footer.jsp" %>
