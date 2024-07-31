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
                              <span class="time">2016-01-15 18:47</span>
                              <a href="/userPage?action=detail&seq=${question.userSeq}" class="author">${question.writer}</a>
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
                      <li><a href="#">«</a></li>
                      <li><a href="#">1</a></li>
                      <li><a href="#">2</a></li>
                      <li><a href="#">3</a></li>
                      <li><a href="#">4</a></li>
                      <li><a href="#">5</a></li>
                      <li><a href="#">»</a></li>
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
