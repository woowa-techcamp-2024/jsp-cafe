<%@ page import="org.example.jspcafe.question.Question" %>
<%@ page import="org.example.jspcafe.reply.Reply" %>
<%@ page import="org.example.jspcafe.user.User" %>
<%@ page import="static org.example.jspcafe.common.DateTimeUtil.dateTimeToString" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="../header.jsp" %>
<%@ include file="../navbar.jsp" %>

<%
    // request에서 "question" 객체를 가져옵니다.
    Question question = (Question) request.getAttribute("question");
    List<Reply> replies = (List<Reply>) request.getAttribute("replies");
%>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
            <header class="qna-header">
                <h2 class="qna-title"><%= question.getTitle() %>
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
                            <a href="/users/<%= question.getUserId() %>"
                               class="article-author-name"><%= question.getUser().getNickname() %>
                            </a>
                            <a href="/questions/<%= question.getId() %>" class="article-header-time" title="퍼머링크">
                                <%= dateTimeToString(question.getLastModifiedDate()) %>
                                <i class="icon-link"></i>
                            </a>
                        </div>
                    </div>
                    <div class="article-doc">
                        <p><%= question.getContents() %>
                        </p>
                    </div>
                    <div class="article-util">
                        <ul class="article-util-list">
                            <li>
                                <a class="link-modify-article" href="/questions/<%= question.getId() %>/edit">수정</a>
                            </li>
                            <li>
                                <form class="form-delete" action="/questions/<%= question.getId() %>" method="POST">
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
                        <p class="qna-comment-count"><strong><c:out value="${replies.size()}"/></strong>개의 의견</p>
                        <div class="qna-comment-slipp-articles" id="reply-container">
                            <c:forEach var="reply" items="${replies}">
                                <article class="article" id="reply-${reply.id}">
                                    <div class="article-header">
                                        <div class="article-header-thumb">
                                            <img src="https://graph.facebook.com/v2.3/1324855987/picture"
                                                 class="article-author-thumb" alt="">
                                        </div>
                                        <div class="article-header-text">
                                            <a href="/users/${reply.user.id}"
                                               class="article-author-name">${reply.user.nickname}</a>
                                            <a href="#reply-${reply.id}" class="article-header-time" title="퍼머링크">
                                                    ${dateTimeToString(reply.lastModifiedDate)}
                                            </a>
                                        </div>
                                    </div>
                                    <div class="article-doc comment-doc">
                                        <p>${reply.contents}</p>
                                    </div>
                                    <div class="article-util">
                                        <ul class="article-util-list">
                                            <li>
                                                <a class="link-modify-article"
                                                   href="/questions/${question.id}/answers/${reply.id}/form">수정</a>
                                            </li>
                                            <li>
                                                <form class="delete-reply-form" data-reply-id="${reply.id}"
                                                      action="/questions/${question.id}/answers/${reply.id}"
                                                      method="POST">
                                                    <input type="hidden" name="_method" value="DELETE">
                                                    <button type="submit" class="delete-reply-button">삭제</button>
                                                </form>
                                            </li>
                                        </ul>
                                    </div>
                                </article>
                            </c:forEach>
                            <form id="reply-form" class="submit-write">
                                <div class="form-group" style="padding:14px;">
                                    <textarea id="reply-contents" class="form-control"
                                              placeholder="Update your status"></textarea>
                                </div>
                                <button id="submit-reply" class="btn btn-success pull-right" type="button">답변하기</button>
                                <div class="clearfix"/>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $('#submit-reply').on('click', function () {
            const contents = $('#reply-contents').val();

            if (!contents.trim()) {
                alert('댓글 내용을 입력하세요.');
                return;
            }

            const params = new URLSearchParams();
            params.append('contents', contents);

            axios.post(`/reply?questionId=` + <%= question.getId() %>, params, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })
                .then(function (response) {
                    const createdReply = response.data;
                    console.log(response)
                    console.log(createdReply.id);  // 값 확인
                    console.log(createdReply.userId);
                    console.log(createdReply.userNickName);
                    console.log(createdReply.lastModifiedDate);
                    console.log(createdReply.contents);
                    console.log(createdReply.questionId);

                    const newReply = `
                <article class="article" id="reply-`
                        + createdReply.id + `">
                    <div class="article-header">
                        <div class="article-header-thumb">
                            <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">
                        </div>
                        <div class="article-header-text">
                            <a href="/users/` + createdReply.userId + `" class="article-author-name">` + createdReply.userNickName + `</a>
                            <a href="#reply-` + createdReply.id + `" class="article-header-time" title="퍼머링크">
                                ` + createdReply.lastModifiedDate + `
                            </a>
                        </div>
                    </div>
                    <div class="article-doc comment-doc">
                        <p>` + createdReply.contents + `</p>
                    </div>
                    <div class="article-util">
                        <ul class="article-util-list">
                            <li>
                                <a class="link-modify-article" href="/questions/` + createdReply.questionId + `/answers/` + createdReply.id + `/form">수정</a>
                            </li>
                            <li>
                                <form class="delete-reply-form" data-reply-id="` + createdReply.id + `" action="/reply/` + createdReply.id + `" method="POST">
                                    <input type="hidden" name="_method" value="DELETE">
                                    <button type="submit" class="delete-reply-button">삭제</button>
                                </form>
                            </li>
                        </ul>
                    </div>
                </article>
            `;

                    console.log(createdReply)
                    console.log(newReply);

                    const replyContainer = document.getElementById('reply-container');
                    const replyForm = document.getElementById('reply-form');

                    replyContainer.insertAdjacentHTML('beforeend', newReply);
                    replyContainer.insertBefore(replyForm, replyContainer.lastChild);
                })
                .catch(function (error) {
                    console.error(error);
                    alert('댓글 추가 중 오류가 발생했습니다.');
                });
        });

        $(document).on('submit', '.delete-reply-form', function (event) {
            event.preventDefault();
            const replyId = $(this).data('reply-id');
            axios.delete(`/reply?replyId=` + replyId)
                .then(function (response) {
                    $(`#reply-` + replyId).remove();
                })
                .catch(function (error) {
                    console.error(error);
                    alert('댓글 삭제 중 오류가 발생했습니다.');
                });
        });

        // 질문 삭제 요청
        $('.form-delete').on('submit', function (event) {
            event.preventDefault();
            const questionId = <%= question.getId() %>;

            axios.delete(`/questions/` + questionId)
                .then(function (response) {
                    alert('질문이 삭제되었습니다.');
                    window.location.href = '/';
                })
                .catch(function (error) {
                    console.error(error);
                    alert('질문 삭제 중 오류가 발생했습니다.');
                });
        });
    });

</script>

<%@ include file="../footer.jsp" %>
