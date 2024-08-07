<%@ page import="org.example.jspcafe.question.Question" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.jspcafe.question.QuestionPagination" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../header.jsp" %>
<%@ include file="../navbar.jsp" %>

<%
    // request에서 "pagination" 객체를 가져옵니다.
    QuestionPagination pagination = (QuestionPagination) request.getAttribute("pagination");
    List<Question> questions = pagination.getItems();
    int totalPages = pagination.getTotalPages();
    int pageSize = 15; // 페이지 사이즈를 15로 고정
    int currentPage = pagination.getCurrentPage();

    // 시작 페이지와 끝 페이지를 계산합니다.
    int startPage = Math.max(currentPage, 1);
    int endPage = Math.min(startPage + 4, totalPages);

    // 계산된 값들을 request 속성으로 설정합니다.
    request.setAttribute("startPage", startPage);
    request.setAttribute("endPage", endPage);
    request.setAttribute("currentPage", currentPage);
    request.setAttribute("totalPages", totalPages);
    request.setAttribute("pageSize", pageSize);
%>


<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default qna-list">
            <ul class="list">
                <c:forEach var="question" items="${pagination.items}">
                    <li>
                        <div class="wrap">
                            <div class="main">
                                <strong class="subject">
                                    <a href="questions/${question.id}">${question.title}</a>
                                </strong>
                                <div class="auth-info">
                                    <i class="icon-add-comment"></i>
                                    <span class="time">${question.lastModifiedDate}</span>
                                    <a href="user/profile.jsp" class="author">${question.user.nickname}</a>
                                </div>
                            </div>
                        </div>
                    </li>
                </c:forEach>
            </ul>
            <!-- 페이지네이션 부분 -->
            <div class="row">
                <div class="col-md-3"></div>
                <div class="col-md-6 text-center">
                    <ul class="pagination center-block" style="display:inline-block;">
                        <li class="${currentPage == 1 ? 'disabled' : ''}">
                            <a href="${currentPage == 1 ? '#' : '/questions?page='.concat(currentPage - 1).concat('&size=').concat(pageSize)}">«</a>
                        </li>
                        <c:forEach var="i" begin="${startPage}" end="${endPage}">
                            <li class="${i == currentPage ? 'active' : ''}">
                                <a href="/questions?page=${i}&size=${pageSize}">${i}</a>
                            </li>
                        </c:forEach>
                        <li class="${currentPage == totalPages ? 'disabled' : ''}">
                            <a href="${currentPage == totalPages ? '#' : '/questions?page='.concat(currentPage + 1).concat('&size=').concat(pageSize)}">»</a>
                        </li>
                    </ul>
                </div>
                <div class="col-md-3 qna-write">
                    <a href="/questions/form" class="btn btn-primary pull-right" role="button">질문하기</a>
                </div>
            </div>
        </div>
    </div>
</div>

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

<%@ include file="../footer.jsp" %>
