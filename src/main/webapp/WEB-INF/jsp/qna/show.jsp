<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../template/header.jsp"%>
<%@ include file="../template/nav.jsp"%>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
          <header class="qna-header">
              <h2 class="qna-title">${question.title}</h2>
          </header>
          <div class="content-main">
              <article class="article">
                  <div class="article-header">
                      <div class="article-header-thumb">
                          <img src="https://graph.facebook.com/v2.3/100000059371774/picture" class="article-author-thumb" alt="">
                      </div>
                      <div class="article-header-text">
                          <a href="/users/${question.writerId}" class="article-author-name"> ${question.writer}</a>
                          <a href="${pageContext.request.contextPath}/questions/413" class="article-header-time" title="퍼머링크">
                              ${question.createdAt.toString()}
                              <i class="icon-link"></i>
                          </a>
                      </div>
                  </div>
                  <div class="article-doc">
                      <p> ${question.content} </p>
                  </div>
                  <c:if test="${question.writerId == sessionScope.user.id}">
                  <div class="article-util">
                      <ul class="article-util-list">
                          <li>
                              <a class="link-modify-article" href="${pageContext.request.contextPath}/qna/${question.id}/form">수정</a>
                          </li>
                          <li>
                              <button class="link-delete-article" type="button" onclick="deleteQuestion()">삭제</button>
                          </li>
                          <li>
                              <a class="link-modify-article" href="${pageContext.request.contextPath}/">목록</a>
                          </li>
                      </ul>
                  </div>
                  </c:if>
              </article>

              <div class="qna-comment">
                  <div class="qna-comment-slipp">
                      <p class="qna-comment-count"><strong id="answer-count">0</strong>개의 의견</p>
                      <div class="qna-comment-slipp-articles">
                          <form id="reply" class="submit-write">
                              <div class="form-group" style="padding:14px;">
                                  <textarea class="form-control" placeholder="Update your status"></textarea>
                              </div>
                              <button class="btn btn-success pull-right" type="button" onclick="reply()">답변하기</button>
                              <div class="clearfix" />
                          </form>
                      </div>
                  </div>
              </div>
          </div>
        </div>
    </div>
</div>

<script type="text/template" id="answerTemplate">
	<article class="article comment">
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
        {3}
	</article>
</script>

<%@ include file="../template/footer.jsp"%>

<script>
    const deleteQuestion = () => {
        if (confirm("삭제하시겠습니까?")) {
            $.ajax({
                url: '${pageContext.request.contextPath}/questions/${question.id}',
                type: 'DELETE',
                success: function(response) {
                    if (response['result'] === 'success') {
                        window.location.href = '/';
                    }
                },
                error: function(xhr, status, error) {
                    let errorMessage = "알 수 없는 에러 발생. 다시 시도해 주세요.";
                    if (xhr.status === 403 && xhr.responseJSON && xhr.responseJSON.message) {
                        errorMessage = xhr.responseJSON.message;
                    }
                    alert('Error:' + errorMessage);
                }
            });
        }
    }

    const reply = () => {
        const content = $('#reply textarea').val();
        const questionId = ${question.id};
        $.ajax({
            url: '${pageContext.request.contextPath}/replies',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({questionId:questionId, content: content}),
            success: function(response) {
                $('#reply textarea').val('');
                retrieveReplies();
            },
            error: function(xhr, status, error) {
                let errorMessage = "알 수 없는 에러 발생. 다시 시도해 주세요.";
                if (xhr.status === 403 && xhr.responseJSON && xhr.responseJSON.message) {
                    errorMessage = xhr.responseJSON.message;
                }
                alert('Error:' + errorMessage);
            }
        });
    }

    const retrieveReplies = () => {
        $.ajax({
            url: '${pageContext.request.contextPath}/replies',
            type: 'GET',
            data: {questionId: ${question.id}},
            success: function(response) {
                // 기존 댓글 삭제
                $('.comment').remove();
                $('#answer-count').text(response.length);
                const template = $('#answerTemplate').html();
                let html = '';
                const questionId = ${question.id};
                for (let i = 0; i < response.length; i++) {
                    const answer = response[i];
                    let replyUtils = '';
                    if (answer.writerId === ${sessionScope.user.id}) {
                        replyUtils = `<div class="article-util">
                            <ul class="article-util-list">
                                <li>
                                    <a class="link-modify-article" href="/api/qna/updateAnswer/\${answer.id}">수정</a>
                                </li>
                                <li>
                                    <button type="button" class="delete-answer-button" onclick="deleteReply(\${answer.id})">삭제</button>
                                </li>
                            </ul>
                        </div>`;
                    }
                    html += template.format(answer.writer, answer.createdAt, answer.content, replyUtils);
                }

                $('#reply').before(html);
            },
            error: function(xhr, status, error) {
                let errorMessage = "알 수 없는 에러 발생. 다시 시도해 주세요.";
                if (xhr.status === 403 && xhr.responseJSON && xhr.responseJSON.message) {
                    errorMessage = xhr.responseJSON.message;
                }
                alert('Error:' + errorMessage);
            }
        });
    }

    const deleteReply = (replyId) => {
        if (confirm("댓글을 삭제하시겠습니까?")) {
            $.ajax({
                url: '${pageContext.request.contextPath}/replies/' + replyId,
                type: 'DELETE',
                success: function(response) {
                    if (response['result'] === 'success') {
                        retrieveReplies();
                    }
                },
                error: function(xhr, status, error) {
                    let errorMessage = "알 수 없는 에러 발생. 다시 시도해 주세요.";
                    if (xhr.status === 403 && xhr.responseJSON && xhr.responseJSON.message) {
                        errorMessage = xhr.responseJSON.message;
                    }
                    alert('Error:' + errorMessage);
                }
            });
        }
    }
    $(document).ready(() => {
        retrieveReplies();
    });
</script>
