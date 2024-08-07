<%@ page import="cafe.domain.entity.Article" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/components/head.jsp" %>
<body>
<%@ include file="/WEB-INF/components/header.jsp"%>
<%@ include file="/WEB-INF/components/navigation.jsp"%>
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>

<% Article article = (Article) request.getAttribute("article"); %>
<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
          <header class="qna-header">
              <h2 class="qna-title"><%=article.getTitle()%></h2>
          </header>
          <div class="content-main">
              <article class="article">
                  <div class="article-header">
                      <div class="article-header-thumb">
                          <img src="https://graph.facebook.com/v2.3/100000059371774/picture" class="article-author-thumb" alt="">
                      </div>
                      <div class="article-header-text">
                          <a href="/users/92/kimmunsu" class="article-author-name"><%=article.getWriter()%></a>
                          <a href="/questions/413" class="article-header-time" title="퍼머링크">
                              <%=article.getCreated()%>
                              <i class="icon-link"></i>
                          </a>
                      </div>
                  </div>
                  <div class="article-doc">
                      <p><%=article.getContents()%></p>
                  </div>
                  <div class="article-util">
                      <ul class="article-util-list">
                          <li>
                              <a class="link-modify-article" href="/articles/<%=article.getArticleId()%>/update">수정</a>
                          </li>
                          <li>
                              <form id="delete-form" class="form-delete" onsubmit="return false;">
                                  <button class="link-delete-article" type="button" onclick="deleteArticle('<%=article.getArticleId()%>')">삭제</button>
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
                      <div class="qna-comment-slipp-articles">
                          <form class="submit-write" id="answerForm">
                              <div class="form-group" style="padding:14px;">
                                  <textarea class="form-control" id="answerText" placeholder="Update your status"></textarea>
                              </div>
                              <button class="btn btn-success pull-right" type="button" id="submitAnswer" articleId="<%=article.getArticleId()%>">답변하기</button>
                              <div class="clearfix"></div>
                          </form>
                          <div id="commentsSection"></div>
                          <button id="appendButton" type="button" onclick="appendComments()">더보기</button>
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
				<a class="link-modify-article" href="/api/qna/updateAnswer/{3}">수정</a>
			</li>
			<li>
				<form class="delete-answer-form" action="/api/questions/{3}/answers/{4}" method="POST">
					<input type="hidden" name="_method" value="DELETE">
                     <button type="submit" class="delete-answer-button">삭제</button>
				</form>
			</li>
		</ul>
		</div>
	</article>
</script>
<script>
    function deleteArticle(articleId) {
        if (!confirm('정말로 삭제하시겠습니까?')) {
            return;
        }

        fetch("/articles/" + articleId + "/delete", {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    alert('삭제되었습니다.');
                    window.location.href = '/articles';
                } else {
                    alert('삭제가 불가능합니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('An error occurred. Please try again.');
            });
    }
</script>
<script>
  $(document).ready(function() {
    $('#submitAnswer').on('click', function(event) {
      var answerText = $('#answerText').val();
      var articleId = event.target.getAttribute('articleId');
      $('#answerText').val('');

      $.ajax({
          url: '/comments/' + articleId + '/create',
          type: 'POST',
          data: JSON.stringify({ comments: answerText }),
          success: function() {
              loadComments(articleId);
          },
          error: function(error) {
              console.log('Error creating comment:', error);
          }
      });
    });

      $(document).on('click', '.delete-answer-button', function(event) {
          var articleId = event.target.getAttribute('articleId');
          var commentId = event.target.getAttribute('commentId');

          $.ajax({
              url: '/comments/' + commentId + '/delete',
              method: 'DELETE',
              success: function() {
                  loadComments(articleId);
              },
              error: function(error) {
                  console.log('Error deleting comment:', error);
              }
          });
      });
  });

  var json;
  var lastIndex = 0;
  function appendComments() {
        var commentsHtml = '';
        var nextIndex = json.comments.length < lastIndex + 5 ? json.comments.length : lastIndex + 5;
        for (var i = lastIndex; i < nextIndex; i++) {
          var comment = json.comments[i];
          commentsHtml += '<article class="article" id="answer-1406">\n';
          commentsHtml += '  <div class="article-header">\n';
          commentsHtml += '    <div class="article-header-thumb">\n';
          commentsHtml += '      <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">\n';
          commentsHtml += '    </div>\n';
          commentsHtml += '    <div class="article-header-text">\n';
          commentsHtml += '      <a href="/users/' + comment.userId + '" class="article-author-name">' + comment.userId + '</a>\n';
          commentsHtml += '      <a href="#answer-1434" class="article-header-time" title="퍼머링크">\n';
          commentsHtml += '        ' + comment.created + '\n';
          commentsHtml += '        <i class="icon-link"></i>\n';
          commentsHtml += '      </a>\n';
          commentsHtml += '    </div>\n';
          commentsHtml += '  </div>\n';
          commentsHtml += '  <div class="article-doc comment-doc">\n';
          commentsHtml += '    <p>' + comment.contents + '</p>\n';
          commentsHtml += '  </div>\n';
          commentsHtml += '  <div class="article-util">\n';
          commentsHtml += '    <ul class="article-util-list">\n';
          commentsHtml += '      <li>\n';
          commentsHtml += '        <form>\n';
          commentsHtml += '          <input type="hidden" name="_method" value="DELETE">\n';
          commentsHtml += '          <button type="button" class="delete-answer-button" id="deleteAnswer" articleId="' + comment.articleId + '" commentId="' + comment.commentId + '">삭제</button>\n';
          commentsHtml += '        </form>\n';
          commentsHtml += '      </li>\n';
          commentsHtml += '    </ul>\n';
          commentsHtml += '  </div>\n';
          commentsHtml += '</article>\n';
        }
        lastIndex = nextIndex;
        if (nextIndex === json.comments.length) {
            $('#appendButton').remove();
        }
        $('#commentsSection').append(commentsHtml);
  }

  function loadComments(articleId) {
      $.ajax({
          url: '/comments/' + articleId,
          method: 'GET',
          success: function(data) {
              json = JSON.parse(data);

              var commentsHtml = '';
              var commentsLength = json.comments.length < 5 ? json.comments.length : 5;
              lastIndex = commentsLength;
              for (var i = 0; i < commentsLength; i++) {
                  var comment = json.comments[i];
                  commentsHtml += '<article class="article" id="answer-1406">\n';
                  commentsHtml += '  <div class="article-header">\n';
                  commentsHtml += '    <div class="article-header-thumb">\n';
                  commentsHtml += '      <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">\n';
                  commentsHtml += '    </div>\n';
                  commentsHtml += '    <div class="article-header-text">\n';
                  commentsHtml += '      <a href="/users/' + comment.userId + '" class="article-author-name">' + comment.userId + '</a>\n';
                  commentsHtml += '      <a href="#answer-1434" class="article-header-time" title="퍼머링크">\n';
                  commentsHtml += '        ' + comment.created + '\n';
                  commentsHtml += '        <i class="icon-link"></i>\n';
                  commentsHtml += '      </a>\n';
                  commentsHtml += '    </div>\n';
                  commentsHtml += '  </div>\n';
                  commentsHtml += '  <div class="article-doc comment-doc">\n';
                  commentsHtml += '    <p>' + comment.contents + '</p>\n';
                  commentsHtml += '  </div>\n';
                  commentsHtml += '  <div class="article-util">\n';
                  commentsHtml += '    <ul class="article-util-list">\n';
                  commentsHtml += '      <li>\n';
                  commentsHtml += '        <form>\n';
                  commentsHtml += '          <input type="hidden" name="_method" value="DELETE">\n';
                  commentsHtml += '          <button type="button" class="delete-answer-button" id="deleteAnswer" articleId="' + comment.articleId + '" commentId="' + comment.commentId + '">삭제</button>\n';
                  commentsHtml += '        </form>\n';
                  commentsHtml += '      </li>\n';
                  commentsHtml += '    </ul>\n';
                  commentsHtml += '  </div>\n';
                  commentsHtml += '</article>\n';
              }
              if (json.comments.length <= 5) {
                    $('#appendButton').remove();
              }
              $('#commentsSection').html(commentsHtml);
          },
          error: function (error) {
              console.log('Error loading comments:', error);
          }
      });
  }

  document.addEventListener('DOMContentLoaded', function () {
      var urlPath = window.location.pathname;
      var articleId = urlPath.split("/")[2];
      loadComments(articleId);
  });

</script>

<!-- script references -->
<%@ include file="/WEB-INF/components/script.jsp" %>
	</body>
</html>