<%@ page contentType="text/html; charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <title>홈 페이지</title>
    <%@include file="/WEB-INF/includes/head.jsp"%>
</head>
	
<body>

<%@include file="/WEB-INF/includes/navbar-fixed-top.jsp"%>
<%@include file="/WEB-INF/includes/navbar-default.jsp"%>

<div class="container" id="main">
   <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
      <div class="panel panel-default qna-list">
          <ul class="list">
              <c:forEach var="question" items="${questions}" varStatus="status">
              <li>
                  <div class="wrap">
                      <div class="main">
                          <strong class="subject">
                              <a href="/questionPage?action=detail&seq=${question.questionSeq}">${question.title}</a>
                          </strong>
                          <div class="auth-info">
                              <i class="icon-add-comment"></i>
                              <span class="time"></span>
                              <a href="/userPage?action=detail&seq=${question.userSeq}" class="author">${question.writer}</a>
                          </div>
                          <div class="reply" title="댓글">
                              <i class="icon-reply"></i>
                              <span class="point"></span>
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
                      <c:set var="currentPage" value="${param.page != null ? param.page : 1}" />
                      <c:set var="startPage" value="${(currentPage - 1) - ((currentPage - 1) % 5) + 1}" />
                      <c:set var="endPage" value="${startPage + 4}" />
                      <c:set var="endPage" value="${endPage > maxPage ? maxPage : endPage}" />

                      <ul class="pagination">
                          <c:if test="${startPage > 5}">
                              <li><a href="/questionPage?action=list&page=${startPage - 1}">«</a></li>
                          </c:if>

                          <c:forEach begin="${startPage}" end="${endPage}" var="pageNum">
                              <li class="${pageNum == currentPage ? 'active' : ''}">
                                  <a href="/questionPage?action=list&page=${pageNum}">${pageNum}</a>
                              </li>
                          </c:forEach>

                          <c:if test="${endPage < maxPage}">
                              <li><a href="/questionPage?action=list&page=${endPage + 1}">»</a></li>
                          </c:if>
                      </ul>
                </ul>
              </div>
              <%
                  if (userId != null) {
              %>
              <div class="col-md-3 qna-write">
                  <a href="/questionPage?action=register" class="btn btn-primary pull-right" role="button">질문하기</a>
              </div>
              <%
                  }
              %>
          </div>
        </div>
    </div>
</div>

<!-- script references -->
<%@include file="/WEB-INF/includes/script-references.jsp"%>
</body>
</html>
