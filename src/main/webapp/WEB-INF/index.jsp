<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<%@include file="share/header.jsp" %>
<body>
<%@include file="share/navbar.jsp" %>
<%@include file="share/sub_navbar.jsp" %>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default qna-list">
            <ul class="list">
                <c:if test="${articlePage.totalElementsCount eq 0}">
                    <div class="wrap">
                        <div class="main">
                            <p>게시물이 존재하지 않습니다.</p>
                        </div>
                    </div>
                </c:if>
                <c:forEach var="article" items="${articlePage.elements}" varStatus="status">
                    <li>
                        <div class="wrap">
                            <div class="main">
                                <strong class="subject">
                                    <a href="/qna/<c:out value="${article.id}"/>">
                                        <c:out value=" ${article.title}"/>
                                    </a>
                                </strong>
                                <div class="auth-info">
                                    <i class="icon-add-comment"></i>
                                    <span class="time"> ${article.createdAt}</span>
                                    <a href="/users/profile/<c:out value="${article.author.id}"/>" class="author"><c:out
                                            value=" ${article.author.name}"/></a>
                                </div>
                                <div class="reply" title="댓글">
                                    <i class="icon-reply"></i>
                                    <span class="point">${article.replyCount}</span>
                                </div>
                            </div>
                        </div>
                    </li>
                </c:forEach>
            </ul>
            <div class="row">
                <div class="col-md-3"></div>
                <div class="col-md-6 text-center">
                    <ul id="pagination" class="pagination center-block" style="display:inline-block;">
                    </ul>
                </div>
                <div class="col-md-3 qna-write">
                    <a href="/qna/form" class="btn btn-primary pull-right" role="button">질문하기</a>
                </div>
            </div>
        </div>
    </div>
</div>

<!--login modal-->
<!--
<div id="loginModal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
  <div class="modal-contents">
      <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h2 class="text-center"><img src="https://lh5.googleusercontent.com/-b0-k99FZlyE/AAAAAAAAAAI/AAAAAAAAAAA/eu7opA4byxI/photo.jpg?sz=100" class="img-circle"><br>Login</h2>
      </div>
      <div class="modal-body">
          <form class="form col-md-12 center-block">
              <div class="form-group">
                  <label for="userId">사용자 아이디</label>
                  <input class="form-control" name="userId" placeholder="User ID">
              </div>
              <div class="form-group">
                  <label for="password">비밀번호</label>
                  <input type="password" class="form-control" name="password" placeholder="Password">
              </div>
              <div class="form-group">
                  <button class="btn btn-primary btn-lg btn-block">로그인</button>
                  <span class="pull-right"><a href="#registerModal" role="button" data-toggle="modal">회원가입</a></span>
              </div>
          </form>
      </div>
      <div class="modal-footer">
          <div class="col-md-12">
          <button class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
      </div>  
      </div>
  </div>
  </div>
</div>
-->

<!--register modal-->
<!--
<div id="registerModal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
  <div class="modal-contents">
      <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h2 class="text-center"><img src="https://lh5.googleusercontent.com/-b0-k99FZlyE/AAAAAAAAAAI/AAAAAAAAAAA/eu7opA4byxI/photo.jpg?sz=100" class="img-circle"><br>회원가입</h2>
      </div>
      <div class="modal-body">
          <form class="form col-md-12 center-block">
              <div class="form-group">
                  <label for="userId">사용자 아이디</label>
                  <input class="form-control" id="userId" name="userId" placeholder="User ID">
              </div>
              <div class="form-group">
                  <label for="password">비밀번호</label>
                  <input type="password" class="form-control" id="password" name="password" placeholder="Password">
              </div>
              <div class="form-group">
                  <label for="name">이름</label>
                  <input class="form-control" id="name" name="name" placeholder="Name">
              </div>
              <div class="form-group">
                  <label for="email">이메일</label>
                  <input type="email" class="form-control" id="email" name="email" placeholder="Email">
              </div>
            <div class="form-group">
              <button class="btn btn-primary btn-lg btn-block">회원가입</button>
            </div>
          </form>
      </div>
      <div class="modal-footer">
          <div class="col-md-12">
          <button class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
      </div>  
      </div>
  </div>
  </div>
</div>
-->

<%@include file="share/footer.jsp" %>

<script>
    var currentPage = ${param.page != null ? param.page : 1};
    var totalPages = ${articlePage.totalPage};

    document.addEventListener('DOMContentLoaded', function() {
        createPagination(currentPage, totalPages);
    });

    function createPagination(currentPage, totalPages) {
        var ul = document.getElementById('pagination');
        ul.innerHTML = '';

        var startPage = Math.floor((currentPage - 1) / 5) * 5 + 1;
        var endPage = Math.min(startPage + 4, totalPages);

        // 이전 페이지 그룹 버튼
        if (startPage > 1) {
            var li = document.createElement('li');
            li.innerHTML = '<a href="?page=' + (startPage - 1) + '">«</a>';
            ul.appendChild(li);
        }

        // 페이지 번호 버튼
        for (var i = startPage; i <= endPage; i++) {
            var li = document.createElement('li');
            if (i === currentPage) {
                li.className = 'active';
            }
            li.innerHTML = '<a href="?page=' + i + '">' + i + '</a>';
            ul.appendChild(li);
        }

        // 다음 페이지 그룹 버튼
        if (endPage < totalPages) {
            var li = document.createElement('li');
            li.innerHTML = '<a href="?page=' + (endPage + 1) + '">»</a>';
            ul.appendChild(li);
        }
    }
</script>
</body>
</html>