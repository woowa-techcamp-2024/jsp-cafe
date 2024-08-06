<%@ page import="com.woowa.cafe.domain.Article" %>
<%@ page import="java.util.List" %>
<%@ page import="com.woowa.cafe.dto.article.ArticleDto" %>
<%@ page import="com.woowa.cafe.dto.article.ArticleListDto" %>
<%@ page import="com.woowa.cafe.dto.article.ArticlePageDto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<%@ include file="/WEB-INF/components/header.html" %>
<body>
<%@ include file="/WEB-INF/components/navbar-fixed-top-header.html" %>
<%@ include file="/WEB-INF/components/navbar-default.jsp" %>
<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default qna-list">
            <ul class="list">
                <%
                    ArticlePageDto articleDtos = (ArticlePageDto) request.getAttribute("articleDtos");
                    if (articleDtos != null) {
                        for (ArticleListDto article : articleDtos.articles()) {
                %>
                <li>
                    <div class="wrap">
                        <div class="main">
                            <strong class="subject">
                                <a href="<%= "/question/" + article.articleId() %>"><%= article.title() %>
                                </a>
                            </strong>
                            <div class="auth-info">
                                <i class="icon-add-comment"></i>
                                <span class="time"><%= article.updatedAt()%></span>
                                <a href="<%= "/user/" + article.writerId()%>"
                                   class="author"><%= article.writerName()%>
                                </a>
                            </div>
                            <div class="reply" title="댓글">
                                <i class="icon-reply"></i>
                                <span class="point"><%=article.replyCount()%></span>
                            </div>
                        </div>
                    </div>
                </li>
                <% }
                }%>
            </ul>
            <div class="row">
                <div class="col-md-3"></div>
                <div class="col-md-6 text-center">
                    <ul class="pagination center-block" style="display:inline-block;">
                    </ul>
                </div>
                <div class="col-md-3 qna-write">
                    <a href="/question" class="btn btn-primary pull-right" role="button">질문하기</a>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    document.addEventListener('DOMContentLoaded', (event) => {
        const pageSize = 15;
        const paginationContainer = document.querySelector('.pagination');
        paginationContainer.innerHTML = '';

        const page = <%= articleDtos.currentPage() %>;
        const hasNextPages =
        <%= articleDtos.nextPages() %>
        const hasPreviousPages = <%= articleDtos.currentPage() %> / 5 > 1;
        const totalCount = <%= articleDtos.totalCount() %>;
        const lastPage = Math.ceil(totalCount / pageSize);

        if (hasPreviousPages) {
            paginationContainer.innerHTML += `<li><a href="/?page=` + (page - 5) + `&size=15">«</a></li>`;
        }

        if (hasNextPages) {
            const startPage = Math.floor((page - 1) / 5) * 5 + 1;
            const endPage = Math.min(startPage + 4, lastPage);

            for (let i = startPage; i <= endPage; i++) {
                paginationContainer.innerHTML += `<li><a href="/?page=` + i + `">` + i + `</a></li>`;
            }

            if (endPage < lastPage) {
                paginationContainer.innerHTML += `<li><a href="/?page=` + (endPage + 1) + `">»</a></li>`;
            }
        } else {
            console.log(lastPage);
            const roundedLastPage = Math.ceil(lastPage * 10) / 10;
            console.log(roundedLastPage);
            for (let i = 0; i < roundedLastPage; i++) {
                const currentPage = Math.floor((lastPage - 1) / 5) * 5 + i;
                paginationContainer.innerHTML += `<li><a href="/?page=` + currentPage + `">` + currentPage + `</a></li>`;
            }
        }
    });
</script>

<%@ include file="/WEB-INF/components/scirpt-refernces.html" %>
</body>
</html>
