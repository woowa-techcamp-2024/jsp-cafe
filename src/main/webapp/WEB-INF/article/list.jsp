<%@ page import="cafe.domain.entity.Article" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../components/head.jsp" %>
<body>
<%@ include file="../components/header.jsp"%>
<%@ include file="../components/navigation.jsp"%>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default qna-list">
            <ul class="list">
                <%
                    int currentPage = Integer.parseInt(request.getParameter("page"));
                    int maxPage = (int) request.getAttribute("maxPage");
                    Map<String, Article> articles = (Map<String, Article>) request.getAttribute("articles");
                    for (String key : articles.keySet()) {
                        Article article = articles.get(key);
                %>
                <li>
                    <div class="wrap">
                        <div class="main">
                            <strong class="subject">
                                <a href="articles/<%=key%>"><%=article.getTitle()%></a>
                            </strong>
                            <div class="auth-info">
                                <i class="icon-add-comment"></i>
                                <span class="time"><%=article.getContents()%></span>
                                <a href="/WEB-INF/user/profile.jsp" class="author"><%=article.getWriter()%></a>
                            </div>
                            <div class="reply" title="댓글">
                                <i class="icon-reply"></i>
                                <span class="point">0</span>
                            </div>
                        </div>
                    </div>
                </li>
                <%
                    }
                %>
            </ul>
            <div class="row">
                <div class="col-md-3"></div>
                <div class="col-md-6 text-center">
                    <ul class="pagination center-block" style="display:inline-block;">
                        <%
                            if ((currentPage - 1) / 5 != 0) {
                                int before = ((currentPage - 1) / 5) * 5;
                        %>
                            <li><a href="/articles?page=<%=before%>">«</a></li>
                        <% } %>

                        <%
                            int start = ((currentPage - 1) / 5) * 5 + 1;
                            int end = Math.min(start + 4, maxPage);
                            for (int i = start; i <= end; i++) {
                        %>
                            <li><a href="/articles?page=<%=i%>"><%=i%></a></li>
                        <% } %>

                        <%
                            if ((currentPage - 1) / 5 < (maxPage - 1) / 5) {
                                int next = ((currentPage - 1) / 5 + 1) * 5 + 1;
                        %>
                            <li><a href="/articles?page=<%=next%>">»</a></li>
                        <% } %>
                    </ul>
                </div>
                <div class="col-md-3 qna-write">
                    <a href="/articles/create" class="btn btn-primary pull-right" role="button">질문하기</a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- script references -->
<%@ include file="/WEB-INF/components/script.jsp" %>
</body>
</html>