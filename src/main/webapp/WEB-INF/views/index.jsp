<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="common/header.jsp"/>

<body>
<div class="container">
    <div class="banner">
        <p>JSP Cafe Mission !</p>
        <H2>김준기 카페입니다 !</H2>
    </div>

    <p style="color: #888888;">전체 글 N개</p>

    <table>
        <thead>
        <tr>
            <th>제목</th>
            <th>작성자</th>
            <th>작성일자</th>
            <th>조회수</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="article" items="${articles}">
            <tr>
                <td><a href="${pageContext.request.contextPath}/articles/${article.articleId}">${article.title}</a></td>
                <td>${article.authorNickname}</td>
                <td>${article.createdAt}</td>
                <td>${article.hits}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div style="display: flex; justify-content: space-between; margin-top: 20px;">
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="${pageContext.request.contextPath}/articles?page=${currentPage - 1}">&lt;</a>
            </c:if>
            <c:forEach begin="1" end="${totalPages}" var="i">
                <c:choose>
                    <c:when test="${i == currentPage}">
                        <a href="#" class="active">${i}</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/articles?page=${i}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <a href="${pageContext.request.contextPath}/articles?page=${currentPage + 1}">&gt;</a>
            </c:if>
        </div>

        <%--        <form>--%>
        <%--            <input type="text" placeholder="검색어를 입력하세요" style="color: #888888;">--%>
        <a href="${pageContext.request.contextPath}/articles/write" class="btn btn-primary pull-right"
           role="button">글쓰기</a>
        <%--            <button>글쓰기</button>--%>
        <%--        </form>--%>
    </div>
</div>
</body>

<jsp:include page="common/footer.jsp"/>
