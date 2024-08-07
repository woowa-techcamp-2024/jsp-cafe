<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="template/header.jsp"%>
<%@ include file="template/nav.jsp"%>
<div class="container" id="main">
   <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
      <div class="panel panel-default qna-list">
          <ul class="list">
              <c:forEach var="question" items="${questions}">
                  <li>
                      <div class="wrap">
                          <div class="main">
                              <strong class="subject">
                                  <a href="/qna/show/${question.id}">${question.title}</a>
                              </strong>
                              <div class="auth-info">
                                  <i class="icon-add-comment"></i>
                                  <span class="time">${question.createdAt.toString().replace('T', ' ')}</span>
                                  <a href="/users/${question.writerId}" class="author">${question.writer}</a>
                              </div>
                              <div class="reply" title="댓글">
                                  <i class="icon-reply"></i>
                                  <span class="point">8</span>
                              </div>
                          </div>
                      </div>
                  </li>
              </c:forEach>
          </ul>
          <div class="row">
              <div class="col-md-3"></div>
              <div class="col-md-6 text-center">
                  <ul class="pagination center-block" style="display:inline-block;">
                      <c:if test="${startPage > 1}">
                        <li><a href="/?p=<c:out value="${startPage - 1}"/>">«</a></li>
                      </c:if>
                      <c:forEach var="i" begin="${startPage}" end="${endPage}">
                          <li><a href="/?p=${i}">${i}</a></li>
                      </c:forEach>
                      <c:if test="${endPage < totalPage}">
                        <li><a href="/?p=<c:out value="${endPage + 1}" />">»</a></li>
                      </c:if>
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
  <div class="modal-content">
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
  <div class="modal-content">
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

<%@ include file="template/footer.jsp"%>
