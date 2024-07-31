<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="kr">
<jsp:include page="../snippet/meta_header.jsp"/>
<body>
<jsp:include page="../snippet/navigation.jsp"/>
<jsp:include page="../snippet/header.jsp"/>


<div class="container" id="main">
    <c:set var="currentQuestion" value="${requestScope.question}"/>
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
            <header class="qna-header">
                <h2 class="qna-title">${currentQuestion.title}</h2>
            </header>
            <div class="content-main">
                <article class="article">
                    <div class="article-header">
                        <div class="article-header-thumb">
                            <img src="https://graph.facebook.com/v2.3/100000059371774/picture"
                                 class="article-author-thumb" alt="">
                        </div>
                        <div class="article-header-text">
                            <a href="/users/92/kimmunsu" class="article-author-name">${currentQuestion.writer}</a>
                            <a href="/questions/413" class="article-header-time" title="퍼머링크">
                                <fmt:parseDate value="${currentQuestion.createdTime}" pattern="yyyy-MM-dd'T'HH:mm"
                                               var="createdTime" type="both"/>
                                <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${createdTime}"/>
                                <i class="icon-link"></i>
                            </a>
                        </div>
                    </div>
                    <div class="article-doc">
                        ${question.contents}
                    </div>
                    <div class="article-util">
                        <ul class="article-util-list">
                            <li>
                                <a id="edit-form-link" class="link-modify-article"
                                   href="/questions/edit/${currentQuestion.id}">수정</a>
                            </li>
                            <li>
                                <form class="form-delete">
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
                        <p class="qna-comment-count"><strong>2</strong>개의 의견</p>
                        <div class="qna-comment-slipp-articles">
                        </div>
                        <form class="submit-write">
                            <input class="form-control" id="questionId" name="questionId" type="hidden"
                                   value="${currentQuestion.id}"
                                   readonly/>
                            <div class="form-group" style="padding:14px;">
                                <textarea class="form-control" name="contents"
                                          placeholder="Update your status"></textarea>
                            </div>
                            <button id="comment-button" class="btn btn-success pull-right" type="submit">답변하기
                            </button>
                            <div class="clearfix"/>
                        </form>
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
                    <a class="link-modify-article" href="#none">수정</a>
                </li>
                <li>
                    <form class="delete-answer-form" action="#none" method="POST">
                        <input type="hidden" name="_method" value="DELETE">
                        <button type="submit" class="delete-answer-button">삭제</button>
                    </form>
                </li>
            </ul>
        </div>
    </article>
</script>

<!-- script references -->
<jsp:include page="../snippet/script.jsp"/>
</body>
</html>

<script>
    $(document).ready(function () {
        $('.form-delete').on('submit', function (e) {
            e.preventDefault();

            $.ajax({
                url: "/questions/delete/${currentQuestion.id}",
                type: 'DELETE',

                success: function () {
                    window.location.href = '/';
                },
                error: function (xhr, status, error) {
                    let errorMessage = xhr.responseText
                    alert(status + ": " + errorMessage)
                    window.location.href = '/';
                }
            });
        })

        $('#edit-form-link').on('click', function (e) {
            e.preventDefault();

            $.ajax({
                url: "/questions/edit/${currentQuestion.id}",
                type: 'GET',

                success: function () {
                    window.location.href = this.url;
                },
                error: function (xhr, status, error) {
                    let errorMessage = xhr.responseText
                    alert(status + ": " + errorMessage)
                    window.location.href = '/';
                }
            });
        })

        $('.submit-write').on('submit', function (e) {
            e.preventDefault();

            let form = $(this);
            let formData = form.serialize();

            $.ajax({
                url: "${pageContext.request.contextPath}/reply",
                type: 'POST',
                data: formData,
                dataType: 'json',

                success: function (response) {
                    console.log(response.writer);
                    console.log(response.createdTime);
                    console.log(response.contents);
                    // 댓글 작성 성공 시 새로운 댓글 HTML 추가
                    var newReplyHTML = createReplyHTML(response.writer, formatTime(response.createdTime), response.contents);
                    $('.qna-comment-slipp-articles').append(newReplyHTML);

                    // 댓글 작성 폼 초기화
                    form.find('textarea').val('');
                },
                error: function (xhr, status, error) {
                    let errorMessage = xhr.responseText
                    alert(status + ": " + errorMessage)
                    window.location.href = "${pageContext.request.contextPath}/questions/${currentQuestion.id}";
                }
            });
        })
    });

    function createReplyHTML(writer, createdTime, contents) {
        // HTML 템플릿 가져오기
        var template = $('#answerTemplate').html();

        // 템플릿에 데이터 치환
        var html = template.replace('{0}', writer)
            .replace('{1}', createdTime)
            .replace('{2}', contents);
        // todo 3, 4번 만들어놓기
        return $(html).get(0);
    }
</script>

