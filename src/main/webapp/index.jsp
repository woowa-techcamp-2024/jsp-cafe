<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--<%@ page import="com.example.entity.Article" %>--%>
<%--<%@ page import="java.util.List" %>--%>
<%--<%@ page import="com.example.service.ArticleService" %>--%>
<%--<!DOCTYPE html>--%>
<%--<html lang="kr">--%>
<%--<%@ include file="WEB-INF/template/head.jsp" %>--%>
<%--<body>--%>
<%--<%@ include file="WEB-INF/template/top-header.jsp" %>--%>
<%--<%@ include file="WEB-INF/template/sub-header.jsp" %>--%>
<%--<div class="container" id="main">--%>
<%--    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">--%>
<%--        <div class="panel panel-default qna-list">--%>
<%--            <ul class="list">--%>
<%--                <%--%>
<%--                    ArticleService articleService = (ArticleService)config.getServletContext()--%>
<%--                            .getAttribute("articleService");--%>
<%--                    long pageNumber;--%>
<%--                    int currentPage;--%>
<%--                    if (request.getParameter("page") == null) {--%>
<%--                        pageNumber = 1;--%>
<%--                    } else {--%>
<%--                        pageNumber = Long.parseLong(request.getParameter("page"));--%>
<%--                    }--%>
<%--                    List<Article> articles = articleService.getArticleByPage(pageNumber);--%>
<%--                    currentPage = (int) pageNumber;--%>
<%--                    int totalPages = ((int) articleService.getTotalPages() + 14) / 15;--%>

<%--                    for (Article article : articles) {--%>
<%--                %>--%>
<%--                <li>--%>
<%--                    <div class="wrap">--%>
<%--                        <div class="main">--%>
<%--                            <strong class="subject">--%>
<%--                                <a href="/articles/<%= article.getId() %>"><%= article.getTitle() %>--%>
<%--                                </a>--%>
<%--                            </strong>--%>
<%--                            <div class="auth-info">--%>
<%--                                <i class="icon-add-comment"></i>--%>
<%--                                <span class="time"><%= article.getCreatedAt() %></span>--%>
<%--                                <a href="users/profile/<%= article.getUserId() %>"--%>
<%--                                   class="author"><%= article.getUserName() %>></a>--%>
<%--                            </div>--%>
<%--                            <div class="reply" title="댓글">--%>
<%--                                <i class="icon-reply"></i>--%>
<%--                                <span class="point">8</span>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </li>--%>
<%--                <% } %>--%>
<%--            </ul>--%>
<%--            <div class="row">--%>
<%--                <div class="col-md-3"></div>--%>
<%--                <div class="col-md-6 text-center">--%>
<%--                    <ul class="pagination center-block" style="display:inline-block;">--%>
<%--                        <%--%>
<%--                            int pageGroupSize = 5;--%>
<%--                            int startPage = ((currentPage - 1) / pageGroupSize) * pageGroupSize + 1;--%>
<%--                            int endPage = Math.min(startPage + pageGroupSize - 1, totalPages);--%>

<%--                            if (startPage > 1) {--%>
<%--                        %>--%>
<%--                        <li><a href="/articles?page=<%= startPage - pageGroupSize %>">«</a></li>--%>
<%--                        <% } %>--%>

<%--                        <%--%>
<%--                            for (int i = startPage; i <= endPage; i++) {--%>
<%--                        %>--%>
<%--                        <li<%= (i == currentPage) ? " class='active'" : "" %>><a href="/articles?page=<%= i %>"><%= i %></a></li>--%>
<%--                        <% } %>--%>

<%--                        <% if (endPage < totalPages) { %>--%>
<%--                        <li><a href="/articles?page=<%= startPage + pageGroupSize %>">»</a></li>--%>
<%--                        <% } %>--%>
<%--                    </ul>--%>
<%--                </div>--%>
<%--                <div class="col-md-3 qna-write">--%>
<%--                    <a href="/articles" class="btn btn-primary pull-right" role="button">질문하기</a>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>

<%--<%@ include file="WEB-INF/template/footer.jsp" %>--%>
<%--</body>--%>
<%--</html>--%>
