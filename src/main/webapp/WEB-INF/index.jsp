<%@ page import="java.util.List" %>
<%@ page import="codesquad.javacafe.post.dto.response.PostResponseDto" %>
<%@ page import="java.util.Objects" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="kr">
<jsp:include page="/common/header.jsp" />
<body>
<jsp:include page="/common/topbar.jsp" />
<jsp:include page="/common/navbar.jsp" />


<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default qna-list">
            <ul class="list">
                <%
                    var postList = (List<PostResponseDto>) request.getAttribute("postList");
                    if (Objects.nonNull(postList)) {
                        for (PostResponseDto post : postList) {
                %>
                <li>
                    <div class="wrap">
                        <div class="main">
                            <strong class="subject">
                                <a href="<%= request.getContextPath() %>/api/post?postId=<%= post.getId() %>"><%= post.getTitle() %></a>
                            </strong>
                            <div class="auth-info">
                                <i class="icon-add-comment"></i>
                                <span class="time"><%= post.getCreatedAt() %></span>
                                <a href="<%= request.getContextPath() %>/user/profile.html" class="author"><%= post.getWriter() %></a>
                            </div>
                            <div class="reply" title="댓글">
                                <i class="icon-reply"></i>
                                <span class="point">8</span>
                            </div>
                        </div>
                    </div>
                </li>
                <%
                        }
                    }
                %>
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
                <div class="col-md-3 qna-write">
                    <a href="<%= request.getContextPath() %>/qna/form.jsp" class="btn btn-primary pull-right" role="button">질문하기</a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- script references -->
<%
    var contextPath = request.getContextPath();
%>
<script src="<%=contextPath%>/js/jquery-2.2.0.min.js"></script>
<script src="<%=contextPath%>/js/bootstrap.min.js"></script>
<script src="<%=contextPath%>/js/scripts.js"></script>
</body>
</html>
